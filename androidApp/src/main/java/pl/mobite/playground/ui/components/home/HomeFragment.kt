package pl.mobite.playground.ui.components.home

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.RecyclerView
import pl.mobite.playground.R
import pl.mobite.playground.domain.home.mvi.impl.HomeViewState
import pl.mobite.playground.ui.components.home.recyclerview.TasksAdapter
import pl.mobite.playground.utils.mviController

class HomeFragment : Fragment(R.layout.fragment_home) {

    // TODO: replace with view binding or butterknife because the kotlinx synthetic does not work in KMM project (yet)
    private val tasksRecyclerView: RecyclerView by lazy { requireView().findViewById(R.id.tasksRecyclerView) }
    private val addTaskButton: Button by lazy { requireView().findViewById(R.id.addTaskButton) }
    private val newTaskInput: EditText by lazy { requireView().findViewById(R.id.newTaskInput) }
    private val deleteCompletedTasksButton: Button by lazy { requireView().findViewById(R.id.deleteCompletedTasksButton) }
    private val progressBar: ProgressBar by lazy { requireView().findViewById(R.id.progressBar) }

    /** Gets HomeViewModel instance and extract mviController from it */
    private val homeMviController by mviController(HomeViewModel::class) { homeMviController }

    private val tasksAdapter = TasksAdapter { taskId, isChecked ->
        /** send action to change tasks state (completed/not completed) */
        homeMviController.accept { updateTask(taskId, isChecked) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tasksRecyclerView.adapter = tasksAdapter

        initButtons()

        /** subscribe with a render function to LifeData with viewStates */
        homeMviController.viewStatesFlow.asLiveData().observe(viewLifecycleOwner, ::render)
    }

    override fun onStart() {
        super.onStart()
        /** send action to load tasks list if list is empty */
        homeMviController.accept { loadDataIfNeeded() }
    }

    private fun initButtons() {
        addTaskButton.setOnClickListener {
            /** send action to add new task */
            homeMviController.accept { addTask(newTaskInput.text.toString()) }
        }

        deleteCompletedTasksButton.setOnClickListener {
            /** send action to delete all completed tasks */
            homeMviController.accept { deleteCompletedTasks() }
        }
    }

    /** Function which updates UI based on new viewState object receives from MviController */
    private fun render(viewState: HomeViewState) {
        with(viewState) {
            progressBar.isVisible = inProgress
            newTaskInput.isEnabled = !inProgress
            addTaskButton.isEnabled = !inProgress

            deleteCompletedTasksButton.isEnabled = tasks.orEmpty().any { it.isDone }

            tasks?.let { newTasks ->
                tasksAdapter.tasks = newTasks
            }

            newTaskAdded?.consume {
                newTaskInput.setText("")
            }

            error?.consume {
                Toast.makeText(requireContext(), "Error occurred", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
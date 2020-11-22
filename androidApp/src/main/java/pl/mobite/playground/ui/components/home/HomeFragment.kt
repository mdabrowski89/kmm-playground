package pl.mobite.playground.ui.components.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.asLiveData
import androidx.fragment.app.Fragment
import pl.mobite.playground.R
import pl.mobite.playground.databinding.FragmentHomeBinding
import pl.mobite.playground.domain.home.mvi.impl.HomeViewState
import pl.mobite.playground.ui.base.viewbinding.viewBinding
import pl.mobite.playground.ui.components.home.recyclerview.TasksAdapter
import pl.mobite.playground.utils.provideFrom
import pl.mobite.playground.utils.with

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding()

    /** Gets HomeViewModel instance and extract mviController from it */
    private val homeMviController by provideFrom(HomeViewModel::class) { homeMviController }
    private val homeEventsCache by provideFrom(HomeViewModel::class) { homeEventsCache }

    private val tasksAdapter = TasksAdapter { taskId, isChecked ->
        /** send action to change tasks state (completed/not completed) */
        homeMviController.accept { updateTask(taskId, isChecked) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.init(
            tasksAdapter = tasksAdapter,
            onAddButtonClick = { homeMviController.accept { addTask(it) }},
            onDeleteButtonClick = { homeMviController.accept { deleteCompletedTasks() } }
        )

        /** subscribe with a render function to LifeData with viewStates */
        homeMviController.viewStatesFlow.asLiveData().observe(viewLifecycleOwner, ::render)
    }

    override fun onStart() {
        super.onStart()
        /** send action to load tasks list if list is empty */
        homeMviController.accept { loadDataIfNeeded() }
    }

    /** Function which updates UI based on new viewState object receives from MviController */
    private fun render(viewState: HomeViewState) = binding.with(viewState) {{
        progressBar.isVisible = inProgress
        newTaskInput.isEnabled = !inProgress
        addTaskButton.isEnabled = !inProgress

        deleteCompletedTasksButton.isEnabled = tasks.orEmpty().any { it.isDone }

        tasks?.let { newTasks ->
            tasksAdapter.tasks = newTasks.toList()
        }

        homeEventsCache.consumeEvent(taskAddedEvent) {
            newTaskInput.setText("")
        }

        homeEventsCache.consumeEvent(errorEvent) {
            Toast.makeText(requireContext(), "Error occurred", Toast.LENGTH_SHORT).show()
        }
    }}
}

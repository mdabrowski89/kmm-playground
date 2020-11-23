package pl.mobite.playground.ui.components.home

import pl.mobite.playground.databinding.FragmentHomeBinding
import pl.mobite.playground.ui.components.home.recyclerview.TasksAdapter

inline fun FragmentHomeBinding.init(
    tasksAdapter: TasksAdapter,
   crossinline onAddButtonClick: (String) -> Unit,
   crossinline onDeleteButtonClick: () -> Unit
) {
    tasksRecyclerView.adapter = tasksAdapter

    addTaskButton.setOnClickListener { onAddButtonClick(newTaskInput.text.toString()) }

    deleteCompletedTasksButton.setOnClickListener { onDeleteButtonClick() }
}
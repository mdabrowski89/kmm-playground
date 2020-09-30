package pl.mobite.playground.ui.components.home.recyclerview

import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import pl.mobite.playground.R
import pl.mobite.playground.model.Task
import pl.mobite.playground.utils.inflateItem

class TaskViewHolder(
    parent: ViewGroup,
    override val containerView: View = parent.inflateItem(R.layout.item_task)
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    private val taskCheck: CheckBox by lazy { containerView.findViewById(R.id.taskCheck) }
    private val taskContent: TextView by lazy { containerView.findViewById(R.id.taskContent) }

    fun bind(item: Task, onCheckChanged: ((Long, Boolean) -> Unit)) {
        taskCheck.setOnCheckedChangeListener(null)
        taskContent.text = item.content
        taskCheck.isChecked = item.isDone
        taskCheck.setOnCheckedChangeListener { _, isChecked ->
            onCheckChanged(item.id, isChecked)
        }
    }
}

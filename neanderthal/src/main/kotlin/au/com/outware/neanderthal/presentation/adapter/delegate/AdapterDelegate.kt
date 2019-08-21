package au.com.outware.neanderthal.presentation.adapter.delegate

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup

/**
 * @author timmutton
 */
interface AdapterDelegate<T> {
    val viewType: Int
    fun isForViewType(items: List<T>, position: Int): Boolean
    fun createViewHolder(parent: ViewGroup): androidx.recyclerview.widget.RecyclerView.ViewHolder
    fun bindViewHolder(items: List<T>, position: Int, holder: androidx.recyclerview.widget.RecyclerView.ViewHolder)
}
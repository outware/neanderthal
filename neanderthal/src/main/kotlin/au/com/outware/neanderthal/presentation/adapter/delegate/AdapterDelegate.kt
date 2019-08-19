package au.com.outware.neanderthal.presentation.adapter.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * @author timmutton
 */
interface AdapterDelegate<T> {
    val viewType: Int
    fun isForViewType(items: List<T>, position: Int): Boolean
    fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    fun bindViewHolder(items: List<T>, position: Int, holder: RecyclerView.ViewHolder)
}
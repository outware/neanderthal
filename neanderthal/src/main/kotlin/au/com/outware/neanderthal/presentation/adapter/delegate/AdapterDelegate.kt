package au.com.outware.neanderthal.presentation.adapter.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import au.com.outware.neanderthal.presentation.adapter.listener.ConfigurationDataListener

/**
 * @author timmutton
 */
interface AdapterDelegate {
    val viewType: Int
    fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    fun bindViewHolder(name: String, value: Any?, type: Class<Any>, holder: RecyclerView.ViewHolder, configurationDataListener: ConfigurationDataListener)
}
package au.com.outware.neanderthal.presentation.adapter.listener

/**
 * Created by huannguyen
 */
interface ConfigurationDataListener {
    fun onDataChanged(configuration: Any?, name: String, value: Any);
}
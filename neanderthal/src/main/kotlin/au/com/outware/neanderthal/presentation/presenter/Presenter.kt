package au.com.outware.neanderthal.presentation.presenter

import android.os.Bundle

/**
 * @author timmutton
 */
interface Presenter {
    fun onCreate(parameters: Bundle?) { }
    fun onStart() { }
    fun onResume() { }
    fun onSaveInstanceState(outState: Bundle) { }
    fun onPause() { }
    fun onStop() { }
    fun onDestroy() { }
}
package au.com.outware.neanderthal.util.extensions

import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.view.View

/**
 * @author timmutton
 */
fun View.snackbar(text: CharSequence, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}): Snackbar {
    val snackbar = Snackbar.make(this, text, duration)
    snackbar.init()
    snackbar.show()
    return snackbar
}

fun View.snackbar(@StringRes text: Int, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}): Snackbar {
    val snackbar = Snackbar.make(this, text, duration)
    snackbar.init()
    snackbar.show()
    return snackbar
}
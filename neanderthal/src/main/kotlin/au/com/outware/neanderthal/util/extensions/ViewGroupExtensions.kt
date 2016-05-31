package au.com.outware.neanderthal.util.extensions

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * @author timmutton
 */
// Inflates a layout and attaches it to itself
fun ViewGroup.inflateLayout(@LayoutRes resource: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(resource, this, attachToRoot)
}

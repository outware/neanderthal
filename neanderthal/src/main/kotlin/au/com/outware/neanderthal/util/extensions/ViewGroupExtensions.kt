package au.com.outware.neanderthal.util.extensions

import android.support.annotation.LayoutRes
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.layoutInflater

/**
 * @author timmutton
 */
// Inflates a layout and attaches it to itself
fun ViewGroup.inflateLayout(@LayoutRes resource: Int, attachToRoot: Boolean = false): View {
    return context.layoutInflater.inflate(resource, this, attachToRoot)
}

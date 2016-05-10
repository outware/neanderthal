package au.com.outware.neanderthal.util.extensions

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable
import java.util.*

@Suppress("UNCHECKED_CAST")
/**
 * @author timmutton
 */
fun Intent.putExtra(extra: Pair<String, Any>) {
    with(extra) {
        when(second) {
            is Boolean -> putExtra(first, second as Boolean)
            is Byte -> putExtra(first, second as Byte)
            is Char -> putExtra(first, second as Char)
            is Short -> putExtra(first, second as Short)
            is Int -> putExtra(first, second as Int)
            is Long -> putExtra(first, second as Long)
            is Float -> putExtra(first, second as Float)
            is Double -> putExtra(first, second as Double)
            is String -> putExtra(first, second as String)
            is CharSequence -> putExtra(first, second as CharSequence)
            is Parcelable -> putExtra(first, second as Parcelable)
            is Bundle -> putExtra(first, second as Bundle)
            is Serializable -> putExtra(first, second as Serializable)
            is IntArray -> putExtra(first, second as IntArray)
            is LongArray -> putExtra(first, second as LongArray)
            is FloatArray -> putExtra(first, second as FloatArray)
            is DoubleArray -> putExtra(first, second as DoubleArray)
            is CharArray -> putExtra(first, second as CharArray)
            is ShortArray -> putExtra(first, second as ShortArray)
            is BooleanArray -> putExtra(first, second as BooleanArray)
            second.javaClass == Array<Parcelable>::class.java -> putExtra(first, second as Array<Parcelable>)
            second.javaClass == Array<String>::class.java -> putExtra(first, second as Array<String>)
            second.javaClass == Array<CharSequence>::class.java -> putExtra(first, second as Array<CharSequence>)
            second.javaClass is ArrayList<*> -> throw UnsupportedOperationException("This method does not accept ArrayList values because of type erasure")
            else -> throw UnsupportedOperationException("Intent extra ${first} does not support type ${second.javaClass.name}")
        }
    }
}
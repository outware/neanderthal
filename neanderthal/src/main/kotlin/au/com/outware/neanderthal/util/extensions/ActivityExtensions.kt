package au.com.outware.neanderthal.util.extensions

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.support.annotation.MenuRes
import android.support.v7.app.AppCompatActivity
import android.view.Menu

/**
 * @author timmutton
 */
fun Activity.isNightMode(): Boolean {
    return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}

fun Activity.inflateMenu(@MenuRes menuRes: Int, menu: Menu?): Boolean {
    menuInflater.inflate(menuRes, menu)
    return true
}

fun Activity.finishWithResult(resultCode: Int, vararg extras: Pair<String, Any>) {
    if(extras.size > 0) {
        val intent = Intent()
        extras.forEach { extra -> intent.putExtra(extra) }
        setResult(resultCode, intent)
    } else {
        setResult(resultCode)
    }

    finish()
}
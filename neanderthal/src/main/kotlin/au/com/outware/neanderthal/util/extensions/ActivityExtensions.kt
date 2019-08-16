package au.com.outware.neanderthal.util.extensions

import android.app.Activity
import android.content.Intent
import androidx.annotation.MenuRes
import android.view.Menu
import kotlin.reflect.KClass

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

fun Activity.navigate(klass: KClass<out Activity>, vararg extras: Pair<String, Any>) {
    val intent = Intent(this, klass.java)
    extras.forEach { extra -> intent.putExtra(extra) }
    startActivity(intent)
}


fun Activity.navigateForResult(klass: KClass<out Activity>, requestCode: Int,
                               vararg extras: Pair<String, Any>) {
    val intent = Intent(this, klass.java)
    extras.forEach { extra -> intent.putExtra(extra) }
    startActivityForResult(intent, requestCode)
}

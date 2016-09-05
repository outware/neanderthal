package au.com.outware.neanderthal.util.widget

import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.EditText
import au.com.outware.neanderthal.util.SimpleTextWatcher

/**
 * @author timmutton
 */
class SingleTextChangedListenerEditText : EditText {
    constructor(ctx: Context) : super(ctx)

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)

    constructor(ctx: Context, attrs: AttributeSet, defStyleAttr: Int) : super(ctx, attrs, defStyleAttr)

    constructor(ctx: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(ctx, attrs,
            defStyleAttr, defStyleRes)

    private var watcher: TextWatcher? = null

    fun setOnTextChangedListener(listener: (CharSequence) -> Unit) {
        if(this.watcher != null) {
            removeTextChangedListener(this.watcher)
        }
        val watcherWrapper = object : SimpleTextWatcher() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                listener(s)
            }
        }

        this.watcher = watcherWrapper
        addTextChangedListener(watcherWrapper)
    }
}
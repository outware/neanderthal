package au.com.outware.neanderthal.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import au.com.outware.neanderthal.data.model.Variant
import au.com.outware.neanderthal.presentation.adapter.delegate.*
import au.com.outware.neanderthal.presentation.presenter.EditVariantPresenter
import au.com.outware.neanderthal.util.extensions.serializableFields
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Field
import java.util.*

/**
 * @author Tim Mutton
 */
class PropertyAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
        EditVariantPresenter.AdapterSurface {
    companion object {
        private val VIEW_TYPE_VARIANT_NAME = 0
        private val VIEW_TYPE_CHARACTER_SEQUENCE = 1
        private val VIEW_TYPE_BOOLEAN = 2
        private val VIEW_TYPE_NUMBER = 3

        fun getPropertyName(configurationProperty: Field): String {
            val name: String
            if (configurationProperty.isAnnotationPresent(SerializedName::class.java)) {
                name = configurationProperty.getAnnotation(SerializedName::class.java).value
            } else {
                name = configurationProperty.name
            }
            return name
        }
    }

    private var variant: Variant? = null
    private val properties: ArrayList<Field> = ArrayList()
    private val delegates = HashSet<AdapterDelegate<Field>>()
    private var setVariantName:Boolean = false

    override fun setItem(variant: Variant) {
        this.variant = variant

        properties.addAll(variant.configuration!!.javaClass.serializableFields.sortedBy { selector -> selector.name })

        setVariantName = (variant.name == null)

        delegates.add(NamePropertyAdapterDelegate(variant, setVariantName, VIEW_TYPE_VARIANT_NAME))
        delegates.add(CharacterSequencePropertyAdapterDelegate(variant, setVariantName, VIEW_TYPE_CHARACTER_SEQUENCE))
        delegates.add(BooleanSequencePropertyAdapterDelegate(variant, setVariantName, VIEW_TYPE_BOOLEAN))
        delegates.add(NumericPropertyAdapterDelegate(variant, setVariantName, VIEW_TYPE_NUMBER))
    }

    override fun getItemViewType(position: Int): Int {
        var viewType = -1

        for(delegate in delegates) {
            if(delegate.isForViewType(properties, position)) {
                viewType = delegate.viewType
                break
            }
        }

        if(viewType == -1) {
            throw IllegalArgumentException("Unsupported property type")
        }

        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null

        for(delegate in delegates) {
            if(viewType == delegate.viewType) {
                viewHolder = delegate.createViewHolder(parent)
                break
            }
        }

        if(viewHolder == null) {
            throw IllegalArgumentException("Unsupported property type")
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = holder.itemViewType
        for(delegate in delegates) {
            if(delegate.viewType == viewType) {
                delegate.bindViewHolder(properties, position, holder)
                break
            }
        }
    }

    override fun getItemCount(): Int {
        return properties.size + if (setVariantName) 1 else 0
    }
}

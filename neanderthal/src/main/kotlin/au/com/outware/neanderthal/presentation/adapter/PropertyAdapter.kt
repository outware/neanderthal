package au.com.outware.neanderthal.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import au.com.outware.neanderthal.data.model.Variant
import au.com.outware.neanderthal.internal.ConfigManager
import au.com.outware.neanderthal.presentation.adapter.delegate.*
import au.com.outware.neanderthal.presentation.adapter.listener.ConfigurationDataListener
import au.com.outware.neanderthal.presentation.presenter.EditVariantPresenter
import java.util.*

/**
 * @author Tim Mutton
 */
class PropertyAdapter(val configManager: ConfigManager<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
        EditVariantPresenter.AdapterSurface, ConfigurationDataListener {
    companion object {
        private val VIEW_TYPE_VARIANT_NAME = 0
        private val VIEW_TYPE_CHARACTER_SEQUENCE = 1
        private val VIEW_TYPE_BOOLEAN = 2
        private val VIEW_TYPE_NUMBER = 3
    }
    private var propertyList: List<String> = ArrayList()
    private var variant: Variant? = null
    private val delegates = HashSet<AdapterDelegate>()
    private var setVariantName:Boolean = false

    override fun setItem(variant: Variant) {
        this.variant = variant
        propertyList = configManager.properties
        setVariantName = (variant.name == null)

        delegates.add(NamePropertyAdapterDelegate(variant, VIEW_TYPE_VARIANT_NAME))
        delegates.add(CharacterSequencePropertyAdapterDelegate(variant, VIEW_TYPE_CHARACTER_SEQUENCE))
        delegates.add(BooleanSequencePropertyAdapterDelegate(variant, VIEW_TYPE_BOOLEAN))
        delegates.add(NumericPropertyAdapterDelegate(variant, VIEW_TYPE_NUMBER))
    }

    override fun getItemViewType(position: Int): Int {
        if(setVariantName && position == 0) {
            return VIEW_TYPE_VARIANT_NAME
        }

        val viewType = configManager.propertyTypes[propertyList[position - if (setVariantName) 1 else 0]]
        if(viewType!!.equals(CharSequence::class.java) || viewType.equals(String::class.java)) {
            return VIEW_TYPE_CHARACTER_SEQUENCE
        }
        if(viewType.equals(Boolean::class.java)) {
            return VIEW_TYPE_BOOLEAN
        }
        return VIEW_TYPE_NUMBER
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
                if(position == 0 && setVariantName) {
                    delegate.bindViewHolder("", null, Any::class.java, holder, this)
                } else {
                    val propertyName = propertyList[position - if (setVariantName) 1 else 0]
                    val displayName = configManager.getPropertyDisplayName(variant!!.configuration, propertyName);
                    val value = configManager.getPropertyValue(variant!!.configuration, propertyName)
                    val type = configManager.getPropertyType(variant!!.configuration, propertyName)
                    delegate.bindViewHolder(displayName, value, type as Class<Any>, holder, this)
                }
                break
            }
        }
    }

    override fun onDataChanged(configuration: Any?, name: String, value: Any) {
        configManager.saveConfiguration(configuration, name, value);
    }

    override fun getItemCount(): Int {
        return propertyList.size + if (setVariantName) 1 else 0
    }
}

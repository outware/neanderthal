package au.com.outware.neanderthal.presentation.presenter

import android.os.Bundle
import au.com.outware.neanderthal.Neanderthal
import au.com.outware.neanderthal.data.model.Variant
import au.com.outware.neanderthal.util.extensions.serializableFields

/**
 * @author timmutton
 */
class EditVariantPresenter constructor(val view: ViewSurface,
                                       val adapter: AdapterSurface): Presenter {
    private lateinit var variant: Variant
    private var originalConfiguration: Any? = null
    private var newVariant: Boolean = false;

    // region Lifecycle
    override fun onCreate(parameters: Bundle?) {
        val name: String? = parameters?.getString(ViewSurface.EXTRA_NAME)
        newVariant = name.isNullOrEmpty()
        variant = getVariant(name)
        if(!newVariant) {
            originalConfiguration = Neanderthal.configurationRepository!!.createConfiguration()
            // Such a hack. Copies the values of a configuration instead of creating a reference to the configuration
            for(field in variant.configuration!!.javaClass.serializableFields) {
                field.set(originalConfiguration!!, field.get(variant.configuration!!))
            }
            view.setDeleteEnabled()
        }
        adapter.setItem(variant)
    }

    override fun onPause() {
        view.dismissDeleteConfirmation()
    }

    // endregion

    fun onDone() {
        if(newVariant && variant.name.isNullOrEmpty()) {
            view.showNameError()
        } else {
            if(variant.name == null) {
                throw IllegalArgumentException("Added or updated variant must have a name")
            }

            Neanderthal.variantRepository?.addVariant(variant)
            Neanderthal.variantRepository?.setCurrentVariant(variant.name!!)
            view.goToVariantList(true, variant.name!!)
        }
    }

    fun onDeleteClicked() {
        view.createDeleteConfirmation()
    }

    fun onBackClicked() {
        if(newVariant && variant.name.isNullOrEmpty()) {
            view.goToVariantList(false, "")
        } else if(originalConfiguration != null) {
            // Check if any changes would be lost
            if(hasChanges()) {
                view.createCancelConfirmation()
            } else {
                view.goToVariantList(false, "")
            }
        } else {
            view.createCancelConfirmation()
        }
    }

    fun onDeleteConfirmation(confirmed: Boolean) {
        if (confirmed) {
            Neanderthal.variantRepository?.removeVariant(variant.name!!)
//            view.notifyDeleted()
        }

        view.dismissDeleteConfirmation()
    }

    fun onCancelConfirmation(confirmed: Boolean) {
        if (confirmed) {
            view.goToVariantList(false, "")
        }

        view.dismissCancelConfirmation()
    }

    private fun getVariant(name: String?): Variant {
        var variant: Variant?

        if(name == null) {
            variant = Variant(null, Neanderthal.configurationRepository!!.createConfiguration())
        } else {
            variant = Neanderthal.variantRepository?.getVariant(name)
            if(variant == null) {
                variant = Variant(null, Neanderthal.configurationRepository!!.createConfiguration())
            }
        }

        return variant
    }

    private fun hasChanges(): Boolean {
        return variant.configuration!!.javaClass.serializableFields.any { field ->
            !field.get(variant.configuration).equals(field.get(originalConfiguration))
        }
    }

    interface ViewSurface {
        companion object {
            val EXTRA_NAME = "extra_name"
            val REQUEST_CODE = 392
        }

        fun goToVariantList(changesSaved: Boolean, name: String)
        fun setDeleteEnabled()
        fun createCancelConfirmation()
        fun dismissCancelConfirmation()
        fun createDeleteConfirmation()
        fun dismissDeleteConfirmation()
        fun showNameError()
    }

    interface AdapterSurface {
        fun setItem(variant: Variant)
    }
}
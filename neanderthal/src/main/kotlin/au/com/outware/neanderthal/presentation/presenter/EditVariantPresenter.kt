package au.com.outware.neanderthal.presentation.presenter

import android.os.Bundle
import au.com.outware.neanderthal.data.model.Variant
import au.com.outware.neanderthal.domain.factory.ConfigurationFactory
import au.com.outware.neanderthal.domain.interactor.VariantInteractor
import au.com.outware.neanderthal.util.extensions.serializableFields
import javax.inject.Inject

/**
 * @author timmutton
 */
class EditVariantPresenter @Inject constructor(): Presenter {
    @Inject
    lateinit internal var variantInteractor: VariantInteractor
    @Inject
    lateinit internal var configurationRepository: ConfigurationFactory

    @Inject
    lateinit internal var view: ViewSurface
    @Inject
    lateinit internal var adapter: AdapterSurface

    lateinit internal var variant: Variant
    internal var originalConfiguration: Any? = null
    internal var newVariant: Boolean = false;

    // region Lifecycle
    override fun onCreate(parameters: Bundle?) {
        var name: String? = parameters?.getString(ViewSurface.EXTRA_NAME)
        newVariant = name.isNullOrEmpty()
        variant = variantInteractor.getVariant(name)
        if(!newVariant) {
            originalConfiguration = configurationRepository.createConfiguration()
            // Such a hack. Copies the values of a configuration instead of creating a reference to the configuration
            for(field in variant.configuration!!.javaClass.serializableFields) {
                field.set(originalConfiguration!!, field.get(variant.configuration!!))
            }
        }
        adapter.setItem(variant)
    }
    // endregion

    fun onDone() {
        if(newVariant && variant.name.isNullOrEmpty()) {
            view.showNameError()
        } else {
            variantInteractor.saveVariant(variant)
            view.goToVariantList(true, variant.name!!)
        }
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

    private fun hasChanges(): Boolean {
        return variant.configuration!!.javaClass.serializableFields.any { field ->
            !field.get(variant.configuration).equals(field.get(originalConfiguration))
        }
    }

    fun onCancelConfirmation(confirmed: Boolean) {
        if (confirmed) {
            view.goToVariantList(false, "")
        }

        view.dismissCancelConfirmation()
    }

    interface ViewSurface {
        companion object {
            val EXTRA_NAME = "extra_name"
            val REQUEST_CODE = 392
        }

        fun goToVariantList(changesSaved: Boolean, name: String)
        fun createCancelConfirmation()
        fun dismissCancelConfirmation()
        fun showNameError()
    }

    interface AdapterSurface {
        fun setItem(variant: Variant)
    }
}
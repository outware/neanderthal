package au.com.outware.neanderthal.presentation.presenter

import android.os.Bundle
import au.com.outware.neanderthal.data.model.Variant
import au.com.outware.neanderthal.domain.interactor.VariantInteractor
import javax.inject.Inject

/**
 * @author timmutton
 */
class EditVariantPresenter @Inject constructor(): Presenter {
    @Inject
    lateinit internal var variantInteractor: VariantInteractor

    @Inject
    lateinit internal var view: ViewSurface
    @Inject
    lateinit internal var adapter: AdapterSurface

    lateinit internal var variant: Variant
    internal var newVariant: Boolean = false;

    // Lifecycle
    override fun onCreate(parameters: Bundle?) {
        var name: String? = parameters?.getString(ViewSurface.EXTRA_NAME)
        newVariant = name.isNullOrEmpty()
        variant = variantInteractor.getVariant(name)
        adapter.setItem(variant)
    }

    fun onDone() {
        if(newVariant && variant.name.isNullOrEmpty()) {
            view.showNameError()
        } else {
            variantInteractor.createOrUpdateVariant(variant)
            view.goToVariantList(true, variant.name!!)
        }
    }

    fun onBackClicked() {
        if(newVariant && variant.name.isNullOrEmpty()) {
            view.goToVariantList(false, "")
        } else {
            view.createCancelConfirmation()
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
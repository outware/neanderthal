package au.com.outware.neanderthal.presentation.presenter

import android.os.Bundle
import au.com.outware.neanderthal.data.model.Variant
import au.com.outware.neanderthal.domain.interactor.VariantInteractor
import java.util.*
import javax.inject.Inject

/**
 * @author timmutton
 */
class VariantListPresenter @Inject constructor(): Presenter {
    companion object {
        val CURRENT_POSITION_KEY = "current_position"
        val CURRENT_VARIANT_NAME_KEY = "current_variant_name"
        val VARIANTS_KEY = "variants"
    }

    @Inject
    lateinit internal var variantInteractor: VariantInteractor

    @Inject
    lateinit internal var view: ViewSurface
    @Inject
    lateinit internal var adapter: AdapterSurface

    lateinit private var currentVariantName: String
    lateinit private var variants: ArrayList<String>
    private var currentPosition = 0

    private var deletedVariant: Variant? = null

    // Lifecycle
    override fun onCreate(parameters: Bundle?) {
        if(parameters != null) {
            currentPosition = parameters.getInt(CURRENT_POSITION_KEY)
            currentVariantName = parameters.getString(CURRENT_VARIANT_NAME_KEY)
            variants = parameters.getStringArrayList(VARIANTS_KEY)
            adapter.setCurrentPosition(currentPosition)
        } else {
            variants = ArrayList<String>(variantInteractor.getVariantNames())
            currentVariantName = variantInteractor.getCurrentVariant()?.name ?: variants.first()
            currentPosition = variants.indexOf(currentVariantName)
            adapter.setCurrentPosition(variants.indexOf(currentVariantName))
        }

        adapter.add(variants)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(CURRENT_POSITION_KEY, currentPosition)
        outState.putString(CURRENT_VARIANT_NAME_KEY, currentVariantName)
        outState.putStringArrayList(VARIANTS_KEY, variants)
    }

    override fun onPause() {
        view.dismissDeleteConfirmation()
    }

    fun onItemSelected(name: String, position: Int) {
        variantInteractor.setCurrentVariant(name)
        currentVariantName = name
        currentPosition = position
        adapter.setCurrentPosition(position)
    }

    fun onAddClicked() {
        view.goToAddVariant()
    }

    fun onEditClicked() {
        view.goToEditVariant(currentVariantName)
    }

    fun onDeleteClicked() {
        view.createDeleteConfirmation()
    }

    fun onAddVariant(name: String) {
        currentVariantName = name
        adapter.add(name)
    }

    fun onDeleteConfirmation(confirmed: Boolean) {
        if (confirmed) {
            deletedVariant = variantInteractor.getCurrentVariant()

            val oldPosition = currentPosition
            variantInteractor.removeVariant(currentVariantName)
            variants.remove(currentVariantName)

            // Set new current variant
            currentPosition = Math.max(--currentPosition, 0)
            currentVariantName = variants[currentPosition]
            variantInteractor.setCurrentVariant(currentVariantName)
            adapter.setCurrentPosition(currentPosition)

            // Notify the views
            adapter.remove(oldPosition)
            view.notifyDeleted()
        }

        view.dismissDeleteConfirmation()
    }

    fun onUndoClicked() {
        deletedVariant?.let {
            variantInteractor.createOrUpdateVariant(deletedVariant!!)
            val name = deletedVariant!!.name!!
            variants.add(name)
            adapter.add(name)
        }
    }

    interface ViewSurface {
        fun createDeleteConfirmation()
        fun dismissDeleteConfirmation()
        fun goToAddVariant()
        fun goToEditVariant(name: String)
        fun notifyDeleted()
    }

    interface AdapterSurface {
        fun setCurrentPosition(position: Int)
        fun add(variants: List<String>)
        fun add(variant: String)
        fun remove(index: Int)
    }
}
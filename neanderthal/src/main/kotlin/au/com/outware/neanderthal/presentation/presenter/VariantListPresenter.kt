package au.com.outware.neanderthal.presentation.presenter

import android.os.Bundle
import au.com.outware.neanderthal.Neanderthal
import au.com.outware.neanderthal.data.model.Variant
import java.util.*

/**
 * @author timmutton
 */
class VariantListPresenter constructor(val view: ViewSurface,
                                               val adapter: AdapterSurface): Presenter {
    private var variants: ArrayList<String> = ArrayList<String>()
    private var currentVariantName: String? = null
    private var currentPosition = 0

    // region Lifecycle
    override fun onCreate(parameters: Bundle?) {
        variants.addAll(getVariantNames())
        if(variants.isNotEmpty()) {
            currentVariantName = Neanderthal.variantRepository?.getCurrentVariant()?.name ?: variants.first()
            currentPosition = variants.indexOf(currentVariantName!!)
            variants.sort()
            adapter.setCurrentPosition(variants.indexOf(currentVariantName!!))
        }

        adapter.add(variants)
    }
    // endregion

    fun onItemSelected(name: String, position: Int) {
        Neanderthal.variantRepository?.setCurrentVariant(name)
        currentVariantName = name
        currentPosition = position
        adapter.setCurrentPosition(position)
    }

    fun onAddClicked() {
        view.goToAddVariant()
    }

    fun onEditClicked() {
        view.goToEditVariant(currentVariantName!!)
    }

    fun onAddVariant(name: String) {
        currentVariantName = name
        variants.add(name)
        variants.sort()
        adapter.add(variants)

        updateEditingEnabled()
    }

    fun onResetToDefaultClicked(){
        view.createResetConfirmation()
    }

    fun onResetConfirmation(confirmed: Boolean){
        if(confirmed) {
            Neanderthal.variantRepository?.resetVariants()
            variants.clear()

            variants.addAll(getVariantNames())

            variants.sort()
            adapter.add(variants)
            currentVariantName = Neanderthal.variantRepository?.getCurrentVariant()?.name ?: variants.firstOrNull()
            currentPosition = variants.indexOf(currentVariantName)
            adapter.setCurrentPosition(currentPosition)
            view.notifyReset()
        }

        view.dismissResetConfirmation()
        updateEditingEnabled()
    }

    fun onLaunchClicked() {
        view.goToMainApplication()
    }

    fun updateEditingEnabled() {
        view.setEditingEnabled(variants.size != 0)
    }

    fun getVariantNames(): List<String> {
        Neanderthal.variantRepository?.let {
            return it.getVariants().map { variant -> variant.name!! }.toList()
        }
        return emptyList<String>()
    }

    interface ViewSurface {
        fun notifyDeleted()
        fun createResetConfirmation()
        fun dismissResetConfirmation()
        fun notifyReset()
        fun goToAddVariant()
        fun goToEditVariant(name: String)
        fun goToMainApplication()
        fun setEditingEnabled(enabled: Boolean)
    }

    interface AdapterSurface {
        fun setCurrentPosition(position: Int)
        fun add(variants: List<String>)
        fun add(variant: String)
        fun remove(index: Int)
    }
}
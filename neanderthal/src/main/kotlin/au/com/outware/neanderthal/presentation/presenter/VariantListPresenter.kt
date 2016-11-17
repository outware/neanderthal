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

    private var deletedVariant: Variant? = null

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

    override fun onPause() {
        view.dismissDeleteConfirmation()
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

    fun onDeleteClicked() {
        view.createDeleteConfirmation()
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

    fun onDeleteConfirmation(confirmed: Boolean) {
        if (confirmed) {
            deletedVariant = Neanderthal.variantRepository?.getCurrentVariant()

            val oldPosition = currentPosition
            Neanderthal.variantRepository?.removeVariant(currentVariantName!!)
            variants.remove(currentVariantName!!)

            if(variants.isNotEmpty()) {
                // Set new current variant
                currentPosition = Math.max(--currentPosition, 0)
                currentVariantName = variants[currentPosition]
                Neanderthal.variantRepository?.setCurrentVariant(currentVariantName!!)
                adapter.setCurrentPosition(currentPosition)
            }else{
                currentPosition = 0
                currentVariantName = ""
            }

            // Notify the views
            adapter.remove(oldPosition)
            view.notifyDeleted()
        }

        view.dismissDeleteConfirmation()
        updateEditingEnabled()
    }

    fun onUndoClicked() {
        deletedVariant?.let {
            val name = deletedVariant!!.name

            if(deletedVariant!!.name == null) {
                throw IllegalArgumentException("Added or updated variant must have a name")
            }

            Neanderthal.variantRepository?.addVariant(deletedVariant!!)
            Neanderthal.variantRepository?.setCurrentVariant(name!!)

            variants.add(name!!)
            adapter.add(name)
        }
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
        fun createDeleteConfirmation()
        fun dismissDeleteConfirmation()
        fun notifyDeleted()
        fun createResetConfirmation()
        fun dismissResetConfirmation()
        fun notifyReset()
        fun goToAddVariant()
        fun goToEditVariant(name: String)
        fun goToMainApplication()
        fun setEditingEnabled(enabled : Boolean)
    }

    interface AdapterSurface {
        fun setCurrentPosition(position: Int)
        fun add(variants: List<String>)
        fun add(variant: String)
        fun remove(index: Int)
    }
}
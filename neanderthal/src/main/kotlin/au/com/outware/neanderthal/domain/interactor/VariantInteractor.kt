package au.com.outware.neanderthal.domain.interactor

import au.com.outware.neanderthal.data.model.Variant

/**
 * @author timmutton
 */
interface VariantInteractor {
    fun createOrUpdateVariant(variant: Variant)
    fun getCurrentVariant(): Variant?
    fun getVariant(name: String?): Variant
    fun getVariantNames(): List<String>
    fun removeVariant(name: String)
    fun setCurrentVariant(name: String)
    fun resetVariants()
}
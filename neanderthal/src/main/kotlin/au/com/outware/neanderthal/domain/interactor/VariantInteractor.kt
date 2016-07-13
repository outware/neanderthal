package au.com.outware.neanderthal.domain.interactor

import au.com.outware.neanderthal.data.model.Variant

/**
 * @author timmutton
 */
interface VariantInteractor {
    fun saveVariant(variant: Variant)
    fun deleteVariant(name: String)
    fun getCurrentVariant(): Variant?
    fun getVariant(name: String?): Variant
    fun getVariantNames(): List<String>
    fun setCurrentVariant(name: String)
    fun resetVariants()
}
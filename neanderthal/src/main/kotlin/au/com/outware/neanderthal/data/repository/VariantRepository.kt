package au.com.outware.neanderthal.data.repository

import au.com.outware.neanderthal.data.model.Variant

/**
 * @author timmutton
 */
interface VariantRepository {
    fun addVariant(variant: Variant)
    fun removeVariant(name: String)

    fun getVariants(): List<Variant>
    fun getVariant(name: String): Variant?

    fun setCurrentVariant(name: String)
    fun getCurrentVariant(): Variant?

    fun resetVariants()
}
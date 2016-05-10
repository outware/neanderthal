package au.com.outware.neanderthal.domain.interactor

import au.com.outware.neanderthal.data.model.Variant
import au.com.outware.neanderthal.data.repository.VariantRepository
import au.com.outware.neanderthal.domain.factory.ConfigurationFactory

/**
 * @author timmutton
 */
class VariantUseCases(val variantRepository: VariantRepository, var configurationRepository: ConfigurationFactory): VariantInteractor {
    override fun createOrUpdateVariant(variant: Variant) {
        if(variant.name == null) {
            throw IllegalArgumentException("Added or updated variant must have a name")
        }

        variantRepository.addVariant(variant)
        variantRepository.setCurrentVariant(variant.name!!)
    }

    override fun getCurrentVariant(): Variant? {
        return variantRepository.getCurrentVariant()
    }

    override fun getVariant(name: String?): Variant {
        var variant: Variant?

        if(name == null) {
            variant = Variant(null, configurationRepository.createConfiguration())
        } else {
            variant = variantRepository.getVariant(name)
            if(variant == null) {
                variant = Variant(null, configurationRepository.createConfiguration())
            }
        }

        return variant
    }

    override fun getVariantNames(): List<String> {
        return variantRepository.getVariants().map { variant -> variant.name!! }.toList()
    }

    override fun removeVariant(name: String) {
        variantRepository.removeVariant(name)
    }

    override fun setCurrentVariant(name: String) {
        variantRepository.setCurrentVariant(name)
    }
}
package au.com.outware.neanderthal

import android.content.Context
import au.com.outware.neanderthal.data.factory.ConfigurationFactoryImpl
import au.com.outware.neanderthal.data.model.ConfigurationFactory
import au.com.outware.neanderthal.data.repository.VariantRepository
import au.com.outware.neanderthal.data.repository.VariantSharedPreferencesRepository

/**
 * @author timmutton
 */
class Neanderthal {
    companion object {
        var variantRepository: VariantRepository? = null
        var configurationRepository: ConfigurationFactory? = null

        @JvmStatic
        fun initialise(context: Context, baseVariants: Map<String, Any>, defaultVariant: String) {
            val klass = baseVariants.get(defaultVariant)!!.javaClass
            variantRepository = VariantSharedPreferencesRepository(klass, context, baseVariants, defaultVariant)
            configurationRepository = ConfigurationFactoryImpl(klass)

        }

        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        fun <T> getConfiguration(): T {
            return variantRepository?.getCurrentVariant()?.configuration as T
        }
    }
}
package au.com.outware.neanderthal.application

import android.content.Context
import au.com.outware.neanderthal.dagger.component.DaggerNeanderthalComponent
import au.com.outware.neanderthal.dagger.component.NeanderthalComponent
import au.com.outware.neanderthal.dagger.module.NeanderthalModule

/**
 * @author timmutton
 */
class Neanderthal {
    companion object {
        @JvmStatic
        lateinit var neanderthalComponent: NeanderthalComponent

        @JvmStatic
        fun initialise(context: Context, baseVariants: Map<String, Any>, defaultVariant: String) {
            val klass = baseVariants.get(defaultVariant)!!.javaClass
            neanderthalComponent = DaggerNeanderthalComponent.builder()
                    .neanderthalModule(NeanderthalModule(context, klass, baseVariants, defaultVariant))
                    .build()
            neanderthalComponent.inject(this)
        }

        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        fun <T> getConfiguration(): T {
            return neanderthalComponent.getVariantInteractor().getCurrentVariant()?.configuration as T
        }
    }
}
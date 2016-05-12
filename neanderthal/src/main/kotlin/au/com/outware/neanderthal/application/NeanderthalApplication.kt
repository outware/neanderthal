package au.com.outware.neanderthal.application

import android.app.Application
import au.com.outware.neanderthal.dagger.component.DaggerNeanderthalApplicationComponent
import au.com.outware.neanderthal.dagger.component.NeanderthalApplicationComponent
import au.com.outware.neanderthal.dagger.module.NeanderthallApplicationModule

/**
 * @author timmutton
 */
open class NeanderthalApplication : Application() {
    companion object {
        @JvmStatic
        lateinit var neanderthalApplicationComponent: NeanderthalApplicationComponent
    }

    fun initialise(klass: Class<out Any>, baseVariants: Map<String, Any>, defaultVariant: String) {
        neanderthalApplicationComponent = DaggerNeanderthalApplicationComponent.builder()
                .neanderthallApplicationModule(NeanderthallApplicationModule(this, klass, baseVariants, defaultVariant))
                .build()
        neanderthalApplicationComponent.inject(this)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getConfiguration(): T {
        return neanderthalApplicationComponent.getVariantInteractor().getCurrentVariant()?.configuration as T
    }
}
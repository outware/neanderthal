package au.com.outware.neanderthal.application

import android.app.Application
import au.com.outware.neanderthal.dagger.component.ApplicationComponent
import au.com.outware.neanderthal.dagger.component.DaggerApplicationComponent
import au.com.outware.neanderthal.dagger.module.ApplicationModule
import au.com.outware.neanderthal.domain.interactor.VariantInteractor
import javax.inject.Inject

/**
 * @author timmutton
 */
open class NeanderthalApplication : Application() {
    companion object {
        @JvmStatic
        lateinit var applicationComponent: ApplicationComponent
    }

    @Inject
    internal lateinit var variantInteractor: VariantInteractor

    fun initialise(klass: Class<out Any>, baseVariants: Map<String, Any>, defaultVariant: String) {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this, klass, baseVariants, defaultVariant))
                .build()
        applicationComponent.inject(this)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getConfiguration(): T {
        return variantInteractor.getCurrentVariant()?.configuration as T
    }
}
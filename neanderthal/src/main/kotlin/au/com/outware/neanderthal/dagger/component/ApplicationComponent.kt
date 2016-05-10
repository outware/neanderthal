package au.com.outware.neanderthal.dagger.component

import android.content.Context
import au.com.outware.neanderthal.application.NeanderthalApplication
import au.com.outware.neanderthal.dagger.module.ApplicationModule
import au.com.outware.neanderthal.data.repository.VariantRepository
import au.com.outware.neanderthal.domain.factory.ConfigurationFactory
import au.com.outware.neanderthal.domain.interactor.VariantInteractor
import dagger.Component
import javax.inject.Singleton

/**
 * @author timmutton
 */
@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
    fun inject(application: NeanderthalApplication)

    fun getApplicationContext(): Context
    fun getVariantRepository(): VariantRepository
    fun getConfigurationRepository(): ConfigurationFactory
    fun getVariantInteractor(): VariantInteractor
}
package au.com.outware.neanderthal.dagger.component

import android.content.Context
import au.com.outware.neanderthal.application.Neanderthal
import au.com.outware.neanderthal.dagger.module.NeanderthalModule
import au.com.outware.neanderthal.data.repository.VariantRepository
import au.com.outware.neanderthal.domain.factory.ConfigurationFactory
import au.com.outware.neanderthal.domain.interactor.VariantInteractor
import dagger.Component
import javax.inject.Singleton

/**
 * @author timmutton
 */
@Singleton
@Component(modules = arrayOf(NeanderthalModule::class))
interface NeanderthalComponent {
    fun inject(neanderthal: Neanderthal.Companion)

    fun getApplicationContext(): Context
    fun getVariantRepository(): VariantRepository
    fun getConfigurationRepository(): ConfigurationFactory
    fun getVariantInteractor(): VariantInteractor
}
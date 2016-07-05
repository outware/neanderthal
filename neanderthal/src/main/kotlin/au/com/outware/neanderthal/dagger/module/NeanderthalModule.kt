package au.com.outware.neanderthal.dagger.module

import android.content.Context
import au.com.outware.neanderthal.data.factory.ConfigurationFactoryImpl
import au.com.outware.neanderthal.data.repository.VariantRepository
import au.com.outware.neanderthal.data.repository.VariantSharedPreferencesRepository
import au.com.outware.neanderthal.domain.factory.ConfigurationFactory
import au.com.outware.neanderthal.domain.interactor.VariantInteractor
import au.com.outware.neanderthal.domain.interactor.VariantUseCases
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author timmutton
 */
@Module
class NeanderthalModule(private val context: Context,
                        private val klass: Class<out Any>,
                        private val baseVariants: Map<String, Any>,
                        private val defaultVariant: String) {
    @Provides
    @Singleton
    fun provideApplicationContext(): Context = context.applicationContext

    @Provides
    @Singleton
    fun provideVariantRepository(): VariantRepository {
        return VariantSharedPreferencesRepository(klass, context, baseVariants, defaultVariant)
    }

    @Provides
    @Singleton
    fun provideConfigurationRepository(): ConfigurationFactory {
        return ConfigurationFactoryImpl(klass)
    }

    @Provides
    @Singleton
    fun provideVariantInteractor(variantRepository: VariantRepository,
                                 configurationRepository: ConfigurationFactory): VariantInteractor {
        return VariantUseCases(variantRepository, configurationRepository)
    }
}
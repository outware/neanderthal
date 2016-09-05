package au.com.outware.neanderthal.dagger.module

import au.com.outware.neanderthal.dagger.scope.PerActivity
import au.com.outware.neanderthal.presentation.presenter.EditVariantPresenter
import dagger.Module
import dagger.Provides

/**
 * @author timmutton
 */
@Module
class EditVariantModule(private val editVariantView: EditVariantPresenter.ViewSurface,
                        private val editVariantAdapter: EditVariantPresenter.AdapterSurface) {
    @Provides
    @PerActivity
    fun provideEditVariantViewSurface(): EditVariantPresenter.ViewSurface = editVariantView

    @Provides
    @PerActivity
    fun provideEditVariantAdapterSurface(): EditVariantPresenter.AdapterSurface = editVariantAdapter
}
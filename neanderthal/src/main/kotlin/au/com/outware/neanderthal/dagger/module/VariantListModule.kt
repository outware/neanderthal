package au.com.outware.neanderthal.dagger.module

import au.com.outware.neanderthal.dagger.scope.PerActivity
import au.com.outware.neanderthal.presentation.presenter.VariantListPresenter
import dagger.Module
import dagger.Provides

/**
 * @author timmutton
 */
@Module
class VariantListModule(private val variantListView: VariantListPresenter.ViewSurface,
                        private val variantListAdapter: VariantListPresenter.AdapterSurface) {
    @Provides
    @PerActivity
    fun provideVariantListViewSurface(): VariantListPresenter.ViewSurface = variantListView

    @Provides
    @PerActivity
    fun provideVariantListAdapterSurface(): VariantListPresenter.AdapterSurface = variantListAdapter
}
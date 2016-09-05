package au.com.outware.neanderthal.dagger.component

import au.com.outware.neanderthal.dagger.module.VariantListModule
import au.com.outware.neanderthal.dagger.scope.PerActivity
import au.com.outware.neanderthal.presentation.view.activity.VariantListActivity
import dagger.Component

/**
 * @author timmutton
 */
@PerActivity
@Component(dependencies = arrayOf(NeanderthalComponent::class), modules = arrayOf(VariantListModule::class))
interface VariantListComponent {
    fun inject(activity: VariantListActivity)
}
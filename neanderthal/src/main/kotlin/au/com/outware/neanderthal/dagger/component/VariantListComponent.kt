package au.com.outware.neanderthal.dagger.component

import au.com.outware.neanderthal.dagger.module.VariantListModule
import au.com.outware.neanderthal.dagger.scope.ActivityScope
import au.com.outware.neanderthal.presentation.view.activity.VariantListActivity
import dagger.Component

/**
 * @author timmutton
 */
@ActivityScope
@Component(dependencies = arrayOf(NeanderthalApplicationComponent::class), modules = arrayOf(VariantListModule::class))
interface VariantListComponent {
    fun inject(activity: VariantListActivity)
}
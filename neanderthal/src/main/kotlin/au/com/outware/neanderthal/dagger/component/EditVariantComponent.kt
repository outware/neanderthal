package au.com.outware.neanderthal.dagger.component

import au.com.outware.neanderthal.dagger.module.EditVariantModule
import au.com.outware.neanderthal.dagger.scope.ActivityScope
import au.com.outware.neanderthal.presentation.view.activity.EditVariantActivity
import dagger.Component

/**
 * @author timmutton
 */
@ActivityScope
@Component(dependencies = arrayOf(NeanderthalComponent::class), modules = arrayOf(EditVariantModule::class))
interface EditVariantComponent {
    fun inject(activity: EditVariantActivity)
}
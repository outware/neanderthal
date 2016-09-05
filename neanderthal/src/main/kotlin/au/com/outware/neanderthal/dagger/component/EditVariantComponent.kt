package au.com.outware.neanderthal.dagger.component

import au.com.outware.neanderthal.dagger.module.EditVariantModule
import au.com.outware.neanderthal.dagger.scope.PerActivity
import au.com.outware.neanderthal.presentation.view.activity.EditVariantActivity
import dagger.Component

/**
 * @author timmutton
 */
@PerActivity
@Component(dependencies = arrayOf(NeanderthalComponent::class), modules = arrayOf(EditVariantModule::class))
interface EditVariantComponent {
    fun inject(activity: EditVariantActivity)
}
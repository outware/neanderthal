package au.com.outware.neanderthal.presentation

import au.com.outware.neanderthal.presentation.presenter.EditVariantPresenter
import com.nhaarman.mockito_kotlin.mock
import org.junit.Before
import org.junit.Test

/**
 * @author timmutton
 */
class EditVariantPresenterTest {
    lateinit var editVariantPresenter: EditVariantPresenter

    @Before
    fun setup() {
        editVariantPresenter = EditVariantPresenter()
        editVariantPresenter.adapter = mock()
        editVariantPresenter.view = mock()
        editVariantPresenter.variantInteractor = mock()
    }

    // TODO: tests

    @Test
    fun test() {

    }
}
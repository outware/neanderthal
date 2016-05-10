package au.com.outware.neanderthal.presentation

import au.com.outware.neanderthal.presentation.presenter.VariantListPresenter
import com.nhaarman.mockito_kotlin.mock
import org.junit.Before
import org.junit.Test

/**
 * @author timmutton
 */
class VariantListPresenterTest {
    lateinit var variantListPresenter: VariantListPresenter

    @Before
    fun setup() {
        variantListPresenter = VariantListPresenter()
        variantListPresenter.view = mock()
        variantListPresenter.variantInteractor = mock()
    }

    // TODO: tests

    @Test
    fun test() {

    }
}
package au.com.outware.neanderthal.domain.interactor

import au.com.outware.neanderthal.data.model.Variant
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.hamcrest.core.Is
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

/**
 * @author timmutton
 */
class VariantUseCasesTest {
    lateinit var variantUseCases: VariantUseCases;

    @Before
    fun setup() {
        variantUseCases = VariantUseCases(mock(), mock())
    }

    @Test
    fun createOrUpdate() {
        // Arrange
        val updateVariant = Variant("test", null)

        // Act
        variantUseCases.saveVariant(updateVariant)

        // Assert
        verify(variantUseCases.variantRepository, times(1)).addVariant(any())
        verify(variantUseCases.variantRepository, times(1)).setCurrentVariant(Mockito.anyString())
    }

    @Test
    fun whenCurrentVariantSet_currentVariantReturned() {
        val variant = Variant("test", null)
        Mockito.`when`(variantUseCases.variantRepository.getCurrentVariant()).thenReturn(variant)

        // Act
        val returnedVariant = variantUseCases.getCurrentVariant()

        // Assert
        verify(variantUseCases.variantRepository, times(1)).getCurrentVariant()
        assertEquals(variant, returnedVariant)
    }

    @Test
    fun whenCurrentVariantNotSet_noVariantReturned() {
        // Act
        val returnedVariant = variantUseCases.getCurrentVariant()

        // Assert
        verify(variantUseCases.variantRepository, times(1)).getCurrentVariant()
        assertNull(returnedVariant)
    }

    @Test
    fun whenNoNamePassed_newConfigurationCreated() {
        // Act
        val variant = variantUseCases.getVariant(null)

        // Assert
        verify(variantUseCases.variantRepository, times(0)).getVariant(Mockito.anyString())
        verify(variantUseCases.configurationRepository, times(1)).createConfiguration()

        assertNull(variant.name)
    }

    @Test
    fun whenVariantExists_configurationReturned() {
        // Arrange
        val variant = Variant("test", null)
        Mockito.`when`(variantUseCases.variantRepository.getVariant(Mockito.anyString())).thenReturn(variant)

        // Act
        val config = variantUseCases.getVariant(variant.name)

        // Assert
        verify(variantUseCases.configurationRepository, times(0)).createConfiguration()
        verify(variantUseCases.variantRepository, times(1)).getVariant(any())
        assertEquals(config, variant)
    }

    @Test
    fun whenVariantsExist_returnVariantNames() {
        // Arrange
        val name = "test"
        val variants = arrayListOf(Variant(name, null))
        Mockito.`when`(variantUseCases.variantRepository.getVariants()).thenReturn(variants)

        // Act
        val returnedNames = variantUseCases.getVariantNames()

        // Assert
        verify(variantUseCases.variantRepository, times(1)).getVariants()
        assertThat(returnedNames.size, Is.`is`(1))
        assertEquals(name, returnedNames[0])
    }

    @Test
    fun whenNoVriantsExist_returnEmptyList() {
        // Arrange
        val variants = emptyList<Variant>()
        Mockito.`when`(variantUseCases.variantRepository.getVariants()).thenReturn(variants)

        // Act
        val returnedNames = variantUseCases.getVariantNames()

        // Assert
        verify(variantUseCases.variantRepository, times(1)).getVariants()
        assertThat(returnedNames.size, Is.`is`(0))
    }

    @Test
    fun removeCurrentVariant() {
        // Act
        variantUseCases.deleteVariant("test")

        // Assert
        verify(variantUseCases.variantRepository, times(1)).removeVariant(Mockito.anyString())
    }

    @Test
    fun setCurrentVariant() {
        // Act
        variantUseCases.setCurrentVariant("test")

        // Assert
        verify(variantUseCases.variantRepository, times(1)).setCurrentVariant(Mockito.anyString())
    }
}
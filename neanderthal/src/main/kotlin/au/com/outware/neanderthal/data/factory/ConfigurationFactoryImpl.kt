package au.com.outware.neanderthal.data.factory

import au.com.outware.neanderthal.domain.factory.ConfigurationFactory

/**
 * @author timmutton
 */
class ConfigurationFactoryImpl(val klass: Class<out Any>): ConfigurationFactory {
    override fun createConfiguration(): Any {
        return klass.newInstance()
    }
}
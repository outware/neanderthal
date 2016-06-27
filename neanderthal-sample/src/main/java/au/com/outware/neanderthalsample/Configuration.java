package au.com.outware.neanderthalsample;

import au.com.outware.neanderthal.annotation.NeanderthalConfiguration;
import au.com.outware.neanderthal.annotation.Property;

/**
 * @author timmutton
 */
@NeanderthalConfiguration
public class Configuration {
    // Fields without 'Property' annotations are ignored by Neanderthal
    private String _id;
    // Annotate properties of the variant configuration
    @Property("Base URL")
    CharSequence baseUrl;
    @Property("Enable Logging")
    boolean enableLogging;
    @Property("Timeout")
    int timeout;

    // Must have a default constructor for GSON to work
    public Configuration() { }

    public Configuration(CharSequence baseUrl, boolean enableLogging, int timeout) {
        this.baseUrl = baseUrl;
        this.enableLogging = enableLogging;
        this.timeout = timeout;
    }

    public CharSequence getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(CharSequence baseUrl) {
        this.baseUrl = baseUrl;
    }

    public boolean isEnableLogging() {
        return enableLogging;
    }

    public void setEnableLogging(boolean enableLogging) {
        this.enableLogging = enableLogging;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}

package au.com.outware.neanderthalsample;

import com.google.gson.annotations.SerializedName;

/**
 * @author timmutton
 */
public class Configuration {
    // Private fields are ignored by Neanderthal
    private String _id;
    // Neanderthal supports the serialized name attribute from GSON
    @SerializedName("Base URL")
    CharSequence baseUrl;
    @SerializedName("Enable Logging")
    boolean enableLogging;
    // If you assign a value to a variable, that value will be the default when creating a new variant
    @SerializedName("Timeout")
    int timeout = 10_000;

    // Must have a default constructor for GSON to work
    public Configuration() { }

    public Configuration(CharSequence baseUrl, boolean enableLogging, int timeout) {
        this.baseUrl = baseUrl;
        this.enableLogging = enableLogging;
        this.timeout = timeout;
    }
}

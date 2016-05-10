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
    @SerializedName("Timeout")
    int timeout;

    // Must have a default constructor for GSON to work
    public Configuration() { }

    public Configuration(CharSequence baseUrl, boolean enableLogging, int timeout) {
        this.baseUrl = baseUrl;
        this.enableLogging = enableLogging;
        this.timeout = timeout;
    }
}

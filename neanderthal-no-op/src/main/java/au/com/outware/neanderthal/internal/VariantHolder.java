package au.com.outware.neanderthal.internal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

/**
 * Created by huannguyen
 */
public class VariantHolder<T> {
    String defaultVariant;
    Map<String, T> variants;

    public VariantHolder(String defaultVariant, Map<String, T> baseVariants) {
        this.defaultVariant = defaultVariant;
        this.variants = baseVariants;
    }

    @SuppressWarnings("unchecked")
    public static VariantHolder parseJson(InputStream jsonInputStream)  throws IOException {
        Gson gson = new GsonBuilder().registerTypeAdapter(CharSequence.class, new CharSequenceDeserializer()).create();
        Reader reader = new InputStreamReader(jsonInputStream);
        return gson.fromJson(reader, VariantHolder.class);
    }

    public String getDefaultVariant() {
        return defaultVariant;
    }

    public Map<String, T> getBaseVariants() {
        return variants;
    }
}

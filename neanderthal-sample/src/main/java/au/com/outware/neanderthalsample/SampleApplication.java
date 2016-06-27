package au.com.outware.neanderthalsample;

import android.support.v7.app.AppCompatDelegate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import au.com.outware.neanderthal.Neanderthal;
import au.com.outware.neanderthal.annotation.NeanderthalApp;
import au.com.outware.neanderthal.application.NeanderthalApplication;

/**
 * @author timmutton
 */
@NeanderthalApp
public class SampleApplication extends NeanderthalApplication {
    public static final String PRODUCTION = "Production";
    public static final String STAGING = "Staging";
    public static final String DEBUG = "Debug";
    public static final String VARIANTS_FILE = "variants.json";

    // If your primary app supports Day/Night mode, so will Neanderthal
    static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            Neanderthal.setup(this, getAssets().open(VARIANTS_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//            initialiseFromJson();
//        } catch (IOException e) {
//            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
//        }
//        initialiseByHand();
    }

    // An example of how you could initialise Neanderthal from a local JSON file
    private void initialiseFromJson() throws IOException {
        Gson gson = new GsonBuilder().registerTypeAdapter(CharSequence.class, new CharSequenceDeserializer()).create();
        Reader reader = new InputStreamReader(getAssets().open(VARIANTS_FILE));
        BaseVariants baseVariants = gson.fromJson(reader, BaseVariants.class);
        Neanderthal.setup(this, baseVariants.variants, baseVariants.defaultVariant);
    }

    // An example of how you could initialise Neanderthal entirely within code
    private void initialiseByHand() {
        Configuration production = new Configuration("api.example.com", false, 2000);
        Configuration staging = new Configuration("api-staging.example.com", false, 2000);
        Configuration debug = new Configuration("api-dev.example.com", true, 1000);
        Map<String, Configuration> baseVariants = new HashMap<>();
        baseVariants.put(PRODUCTION, production);
        baseVariants.put(STAGING, staging);
        baseVariants.put(DEBUG, debug);
        Neanderthal.setup(this, baseVariants, PRODUCTION);
    }
}

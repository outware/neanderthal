package au.com.outware.neanderthalsample;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import au.com.outware.neanderthal.Neanderthal;

/**
 * @author timmutton
 */
public class SampleApplication extends Application {
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

        initialiseFromJson();
//        initialiseByHand();
    }

    // An example of how you could initialise Neanderthal from a local JSON file
    private void initialiseFromJson(){
        try {
            Gson gson = new GsonBuilder().registerTypeAdapter(CharSequence.class, new CharSequenceDeserializer()).create();
            Reader reader = new InputStreamReader(getAssets().open(VARIANTS_FILE));
            BaseVariants baseVariants = gson.fromJson(reader, BaseVariants.class);
            Neanderthal.initialise(this, baseVariants.variants, baseVariants.defaultVariant);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        Neanderthal.initialise(this, baseVariants, PRODUCTION);
    }
}

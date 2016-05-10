package au.com.outware.neanderthalsample;

import android.support.v7.app.AppCompatDelegate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import au.com.outware.neanderthal.application.NeanderthalApplication;
import au.com.outware.neanderthal.util.CharSequenceDeserializer;

/**
 * @author timmutton
 */
public class SampleApplication extends NeanderthalApplication {
    public static final String PRODUCTION = "Production";
    public static final String STAGING = "Staging";
    public static final String DEBUG = "Debug";

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
    private void initialiseFromJson() {
        Gson gson = new GsonBuilder().registerTypeAdapter(CharSequence.class, new CharSequenceDeserializer()).create();
        Reader reader = new InputStreamReader(getResources().openRawResource(R.raw.variants));
        BaseVariants baseVariants = gson.fromJson(reader, BaseVariants.class);
        initialise(Configuration.class, baseVariants.variants, baseVariants.defaultVariant);
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
        initialise(Configuration.class, baseVariants, PRODUCTION);
    }
}

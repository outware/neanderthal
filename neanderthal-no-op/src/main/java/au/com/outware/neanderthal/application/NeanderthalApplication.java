package au.com.outware.neanderthal.application;

import android.app.Application;

import java.util.Map;

/**
 * @author timmutton
 */
public class NeanderthalApplication extends Application {
    private Object variant;

    public <T> void initialise(Class<T> klass, Map<String, T> baseVariants, String defaultVariant) {
        variant = baseVariants.get(defaultVariant);
    }

    @SuppressWarnings("unchecked")
    public <T> T getConfiguration() {
        return (T)variant;
    }
}

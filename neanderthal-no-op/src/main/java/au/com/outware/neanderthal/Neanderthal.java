package au.com.outware.neanderthal;

import android.content.Context;

import java.util.Map;

/**
 * @author timmutton
 */
public class Neanderthal {
    private static Object variant;

    public static <T> void initialise(Context context, Map<String, T> baseVariants, String defaultVariant) {
        variant = baseVariants.get(defaultVariant);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getConfiguration() {
        return (T)variant;
    }
}

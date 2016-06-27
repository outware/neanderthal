package au.com.outware.neanderthal;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import au.com.outware.neanderthal.application.NeanderthalApplication;
import au.com.outware.neanderthal.internal.VariantHolder;

/**
 * Created by huannguyen
 */
public class Neanderthal {

    @SuppressWarnings("unchecked")
    public static void setup(NeanderthalApplication application, InputStream variantConfigInputStream) {
        try {
            VariantHolder variantHolder = VariantHolder.parseJson(variantConfigInputStream);

            // throw exception if default variant and/or base variants data do not exist
            if(variantHolder == null || variantHolder.getDefaultVariant() == null || variantHolder.getDefaultVariant() == null || variantHolder.getBaseVariants().size() == 0) {
                throw new RuntimeException("Unable to obtain data from .json configuration file. Make sure you've specified both base variants and default variant");
            }
            setup(application, variantHolder.getBaseVariants(), variantHolder.getDefaultVariant());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setup(NeanderthalApplication application, Map<String, ?> baseVariants, String defaultVariant) {
        application.initialise(null, baseVariants, defaultVariant);
    }
}

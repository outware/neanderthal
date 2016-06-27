package au.com.outware.neanderthal;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import au.com.outware.neanderthal.annotation.NeanderthalApp;
import au.com.outware.neanderthal.annotation.NeanderthalConfiguration;
import au.com.outware.neanderthal.application.NeanderthalApplication;
import au.com.outware.neanderthal.internal.ConfigManager;
import au.com.outware.neanderthal.internal.ConfigManagerLocator;
import au.com.outware.neanderthal.internal.VariantHolder;

/**
 * Created by huannguyen
 */
public class Neanderthal {
    private static final String TAG = "Neanderthal";
    private static final String NEANDERTHAL_CONFIG_MANAGER_CLASS_NAME = "Neanderthal_ConfigManager";
    private static final String NEANDERTHAL_CONFIG_MANAGER_LOCATORCLASS_NAME = "Neanderthal_ConfigManagerLocator";
    private static boolean debug = false;
    public static ConfigManager sConfigManager;
    private static final String ERROR_CONFIG_MANAGER_NOT_FOUND = String.format("Unable to find the generated Config Manager. Make sure you annotate your Application class with '%s' annotation and annotate your Configuration class with '%s' annotation", NeanderthalApp.class.getSimpleName(), NeanderthalConfiguration.class.getSimpleName());

    /**
     * Set an application up to use Neanderthal.
     *
     * @param application               The application in which Neanderthal is being setup for
     * @param variantConfigInputStream  The input stream containing a configuration .json file data
     */
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

    /**
     * Set an application to to use Neanderthal.
     *
     * @param application       The application in which Neanderthal is being setup for
     * @param baseVariants      The map of variant names and their corresponding configuration objects
     * @param defaultVariant    Name of the default variant
     */
    @SuppressWarnings("unchecked")
    public static void setup(NeanderthalApplication application,  Map<String, ?> baseVariants, String defaultVariant) {
        // get configuration class's package to find generated config manager class
        String appPackageName = application.getClass().getPackage().getName();

        // find config manager
        findConfigManager(getConfigManagerLocation(appPackageName));
        application.initialise(sConfigManager.getConfigurationClass(), baseVariants, defaultVariant);
    }

    /**
     * Set whether the debug mode is enabled.
     *
     * @param debug true indicates the debug mode is ON, false otherwise.
     */
    public static void setDebug(boolean debug) {
        Neanderthal.debug = debug;
    }

    private static void findConfigManager(String packageName) {
        String className = String.format("%s.%s", packageName, NEANDERTHAL_CONFIG_MANAGER_CLASS_NAME);
        try {
            Class<?> configManagerClass = Class.forName(className);
            sConfigManager = (ConfigManager) configManagerClass.newInstance();
            if(debug) {
                Log.d(TAG, "Found config manager: " + sConfigManager.getClass().getCanonicalName());
            }
        } catch (ClassNotFoundException e) {
            showError(ERROR_CONFIG_MANAGER_NOT_FOUND);
        } catch (IllegalAccessException e) {
            showError(ERROR_CONFIG_MANAGER_NOT_FOUND);
        } catch (InstantiationException e) {
            showError(ERROR_CONFIG_MANAGER_NOT_FOUND);
        }
    }

    private static String getConfigManagerLocation(String appPackageName) {
        String className = String.format("%s.%s", appPackageName, NEANDERTHAL_CONFIG_MANAGER_LOCATORCLASS_NAME);
        try {
            Class<?> locatorClass = Class.forName(className);
            ConfigManagerLocator locator = (ConfigManagerLocator) locatorClass.newInstance();
            if(debug) {
                Log.d(TAG, "Found config manager locator: " + locator.getClass().getCanonicalName());
            }
            return locator.getPackageName();
        } catch (ClassNotFoundException e) {
            showError(ERROR_CONFIG_MANAGER_NOT_FOUND);
        } catch (IllegalAccessException e) {
            showError(ERROR_CONFIG_MANAGER_NOT_FOUND);
        } catch (InstantiationException e) {
            showError(ERROR_CONFIG_MANAGER_NOT_FOUND);
        }
        return null;
    }

    private static void showError(String error) {
        throw new RuntimeException(error);
    }
}

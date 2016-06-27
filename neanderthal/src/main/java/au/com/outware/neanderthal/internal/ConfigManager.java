package au.com.outware.neanderthal.internal;

import java.util.List;
import java.util.Map;

/**
 * Created by huannguyen
 */
public abstract class ConfigManager<T> {

    public  abstract Class getConfigurationClass();
    public  abstract List<String> getProperties();
    public  abstract Map<String, String> getPropertyDisplayNames();
    public  abstract Map<String, Class> getPropertyTypes();
    public  abstract String getPropertyDisplayName(T configuration, String propertyName);
    public  abstract Object getPropertyValue(T configuration, String propertyName);
    public  abstract Class<?> getPropertyType(T configuration, String propertyName);
    public  abstract void saveConfiguration(T configuration, String propertyName, Object propertyValue);
    public int getInt(Object object) {
        return (Integer) object;
    }
    public float getFloat(Object object) {
        return (Float) object;
    }
    public double getDouble(Object object) {
        return (Double)object;
    }
    public short getShort(Object object) {
        return (Short)object;
    }
    public long getLong(Object object) {
        return (Long)object;
    }
    public boolean getBoolean(Object object) {
        return (boolean) object;
    }
    public String getString(Object object) {
        return (String)object;
    }
}

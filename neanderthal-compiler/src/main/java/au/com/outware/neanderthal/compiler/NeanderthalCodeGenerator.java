package au.com.outware.neanderthal.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Modifier;

/**
 * Created by huannguyen
 */
public class NeanderthalCodeGenerator {
    private static final String FIELD_PROPERTIES = "properties";
    private static final String FIELD_PROPERTY_NAMES = "propertyNames";
    private static final String FIELD_PROPERTY_TYPES = "propertyTypes";

    private static final String CONFIG_MANAGER_CLASS_NAME = "Neanderthal_ConfigManager";
    private static ClassName CONFIG_MANAGER_BASE_CLASS =
            ClassName.get("au.com.outware.neanderthal.internal", "ConfigManager");
    private static ClassName stringClass = ClassName.get("java.lang", "String");
    private static ClassName listClass = ClassName.get("java.util", "List");
    private static ClassName arrayListClass = ClassName.get("java.util", "ArrayList");
    private static ClassName hashMapClass = ClassName.get("java.util", "HashMap");
    private static ClassName mapClass = ClassName.get("java.util", "Map");
    private static ClassName classClass = ClassName.get("java.lang", "Class");
    private static TypeName listOStrings = ParameterizedTypeName.get(listClass, stringClass);
    private static TypeName mapOfStrings =
            ParameterizedTypeName.get(mapClass, stringClass, stringClass);
    private static TypeName mapOfStringsAndClasses =
            ParameterizedTypeName.get(mapClass, stringClass, classClass);

    private static final String LOCATOR_CLASS_NAME = "Neanderthal_ConfigManagerLocator";
    private static ClassName LOCATOR_BASE_CLASS =
            ClassName.get("au.com.outware.neanderthal.internal", "ConfigManagerLocator");

    /**
     * Generate a sub-class of ConfigManager
     *
     * @param configurationClass    A {@link ClassName} pointing to the configuration class
     * @param propertyNameMap       A {@link Map} of the configuration's properties and their display names
     * @param propertyTypeMap       A {@link Map} of the configuration's properties and their data types
     *
     * @return                      A {@link JavaFile} that contains the generated class.
     */
    public static JavaFile generateConfigManager(ClassName configurationClass, Map<String,
            String> propertyNameMap, Map<String, String> propertyTypeMap) {

        TypeSpec configManagerClass = TypeSpec.classBuilder(CONFIG_MANAGER_CLASS_NAME)
                                              .addModifiers(Modifier.PUBLIC)
                                              .superclass(ParameterizedTypeName
                                                      .get(CONFIG_MANAGER_BASE_CLASS,
                                                              configurationClass))
                                              .addField(listOStrings, FIELD_PROPERTIES, Modifier
                                                      .PRIVATE)
                                              .addField(mapOfStrings, FIELD_PROPERTY_NAMES, Modifier
                                                      .PRIVATE)
                                              .addField(mapOfStringsAndClasses, FIELD_PROPERTY_TYPES, Modifier
                                                      .PRIVATE)
                                              .addMethod(configManagerConstructor(propertyNameMap,
                                                      propertyTypeMap))
                                              .addMethod(getConfigurationClassMethod
                                                      (configurationClass.simpleName()))
                                              .addMethod(getPropertiesMethod())
                                              .addMethod(getPropertyDisplayNamesMethod
                                                      ())
                                              .addMethod(getPropertyTypesMethod())
                                              .addMethod(getPropertyDisplayNameMethod(configurationClass, propertyTypeMap.keySet()))
                                              .addMethod(getPropertyValueMethod(configurationClass, propertyTypeMap.keySet()))
                                              .addMethod(getPropertyTypeMethod(configurationClass, propertyTypeMap.keySet()))
                                              .addMethod(saveConfigurationMethod(configurationClass, propertyNameMap, propertyTypeMap))
                                              .build();

        return buildClass(configurationClass.packageName(), configManagerClass);
    }

    /**
     * Constructor
     */
    private static MethodSpec configManagerConstructor(Map<String, String>
                                                  propertyNameMap, Map<String, String>
            propertyTypeMap) {
        Builder methodBuilder = MethodSpec.constructorBuilder()
                                          .addModifiers(Modifier.PUBLIC);

        // populate properties
        methodBuilder.addStatement("properties = new $T<>()", arrayListClass);

        for (String property : propertyTypeMap.keySet()) {
            methodBuilder.addStatement("properties.add($S)", property);
        }

        // populate property names
        methodBuilder.addStatement("propertyNames = new $T<>()", hashMapClass);

        for (String property : propertyNameMap.keySet()) {
            methodBuilder.addStatement("propertyNames.put($S, $S)", property, propertyNameMap
                    .get(property));
        }

        // populate property data types
        methodBuilder.addStatement("propertyTypes = new $T<>()", hashMapClass);

        for (String property : propertyTypeMap.keySet()) {
            methodBuilder.addStatement("propertyTypes.put($S, $T.class)", property, getClassForDataType(propertyTypeMap
                    .get(property)));
        }
        return methodBuilder.build();
    }

    /**
     * getConfiguration method. This method returns the configuration class.
     *
     */
    private static MethodSpec getConfigurationClassMethod(String configurationClass) {
        return MethodSpec.methodBuilder("getConfigurationClass")
                         .addModifiers(Modifier.PUBLIC)
                         .addAnnotation(Override.class)
                         .returns(Class.class)
                         .addStatement("return " + configurationClass + ".class")
                         .build();
    }

    /**
     * getProperties method. This method returns the list of configuration's properties.
     *
     */
    private static MethodSpec getPropertiesMethod() {
        return MethodSpec.methodBuilder("getProperties")
                         .addModifiers(Modifier.PUBLIC)
                         .addAnnotation(Override.class)
                         .returns(listOStrings)
                         .addStatement(String.format("return %s", FIELD_PROPERTIES))
                         .build();
    }

    /**
     * getPropertyDisplayNames method. This method returns the list of configuration properties' display names (if set in the config .json file).
     *
     */
    private static MethodSpec getPropertyDisplayNamesMethod() {
        return MethodSpec.methodBuilder("getPropertyDisplayNames")
                         .addModifiers(Modifier.PUBLIC)
                         .addAnnotation(Override.class)
                         .returns(mapOfStrings)
                         .addStatement(String.format("return %s", FIELD_PROPERTY_NAMES))
                         .build();
    }

    /**
     * getPropertyTypes method. This method returns the list of configuration properties' data types.
     *
     */
    private static MethodSpec getPropertyTypesMethod() {
        return MethodSpec.methodBuilder("getPropertyTypes")
                         .addModifiers(Modifier.PUBLIC)
                         .addAnnotation(Override.class)
                         .returns(mapOfStringsAndClasses)
                         .addStatement(String.format("return %s", FIELD_PROPERTY_TYPES))
                         .build();
    }

    /**
     * getPropertyValues method. This method returns the value of a property in a configuration.
     *
     */
    private static MethodSpec getPropertyValueMethod(ClassName configurationClassName, Set<String> propertyList) {
        Builder methodBuilder = MethodSpec.methodBuilder("getPropertyValue")
                                          .addModifiers(Modifier.PUBLIC)
                                          .addAnnotation(Override.class)
                                          .addParameter(ClassName.get(configurationClassName.packageName(), configurationClassName.simpleName()), "configuration")
                                          .addParameter(String.class, "propertyName")
                                          .returns(Object.class);

        for(String property: propertyList) {
            methodBuilder.beginControlFlow("if (propertyName == $S)", property)
                         .addStatement(String.format("return ((%s)configuration).%s", configurationClassName.simpleName(), property))
                         .endControlFlow();
        }

        methodBuilder.addStatement("return null");
        return methodBuilder.build();
    }

    /**
     * getPropertyDisplayName method. This method returns the display name of a configuration property.
     *
     */
    private static MethodSpec getPropertyDisplayNameMethod(ClassName configurationClassName, Set<String> propertyList) {
        Builder methodBuilder = MethodSpec.methodBuilder("getPropertyDisplayName")
                                          .addModifiers(Modifier.PUBLIC)
                                          .addAnnotation(Override.class)
                                          .addParameter(ClassName.get(configurationClassName.packageName(), configurationClassName.simpleName()), "configuration")
                                          .addParameter(String.class, "propertyName")
                                          .returns(String.class);

        for(String property: propertyList) {
            methodBuilder.beginControlFlow("if (propertyName == $S)", property)
                         .addStatement("return propertyNames.get($S)", property)
                         .endControlFlow();
        }

        methodBuilder.addStatement("return null");
        return methodBuilder.build();
    }

    /**
     * getPropertyType method. This method returns the data type of a configuration property.
     *
     */
    private static MethodSpec getPropertyTypeMethod(ClassName configurationClassName, Set<String> propertyList) {
        Builder methodBuilder = MethodSpec.methodBuilder("getPropertyType")
                                          .addModifiers(Modifier.PUBLIC)
                                          .addAnnotation(Override.class)
                                          .addParameter(ClassName.get(configurationClassName.packageName(), configurationClassName.simpleName()), "configuration")
                                          .addParameter(String.class, "propertyName")
                                          .returns(Class.class);

        for(String property: propertyList) {
            methodBuilder.beginControlFlow("if (propertyName == $S)", property)
                         .addStatement("return propertyTypes.get($S)", property)
                         .endControlFlow();
        }

        methodBuilder.addStatement("return null");
        return methodBuilder.build();
    }

    /**
     * saveConfiguration method. This method store a configuration's data, given a configuration instance, property display name, and property value.
     *
     */
    private static MethodSpec saveConfigurationMethod(ClassName configurationClassName, Map<String, String> propertyNameMap, Map<String, String> propertyTypeMap) {
        Builder methodBuilder = MethodSpec.methodBuilder("saveConfiguration")
                                          .addModifiers(Modifier.PUBLIC)
                                          .addAnnotation(Override.class)
                                          .addParameter(ClassName.get(configurationClassName.packageName(), configurationClassName.simpleName()), "configuration")
                                          .addParameter(String.class, "propertyDisplayName")
                                          .addParameter(Object.class, "propertyValue")
                                          .returns(void.class);

        for(String property: propertyTypeMap.keySet()) {
            String castMethod = getCastMethod(propertyTypeMap.get(property));
            if(castMethod != null) {
                String propertyDisplayName = propertyNameMap.get(property);
                if(propertyDisplayName == null) {
                    propertyDisplayName = property;
                }
                methodBuilder.beginControlFlow("if (propertyDisplayName == $S)", propertyDisplayName)
                             .addStatement(String.format("((%s)configuration).%s = %s(propertyValue)", configurationClassName.simpleName(), property, castMethod))
                             .endControlFlow();
            }
        }

        return methodBuilder.build();
    }

    public static JavaFile generateConfigManagerLocator(String locatorPackageName, String configManagerPackageName) {
        TypeSpec locatorClass = TypeSpec.classBuilder(LOCATOR_CLASS_NAME)
                                              .addModifiers(Modifier.PUBLIC)
                                              .addSuperinterface(LOCATOR_BASE_CLASS)
                                              .addMethod(getPackageNameMethod(configManagerPackageName))
                                              .build();

        return buildClass(locatorPackageName, locatorClass);
    }

    private static MethodSpec getPackageNameMethod(String configManagerPackageName) {
        return MethodSpec.methodBuilder("getPackageName")
                         .addModifiers(Modifier.PUBLIC)
                         .returns(String.class)
                         .addStatement("return $S", configManagerPackageName)
                         .build();
    }

    private static JavaFile buildClass(String packageName, TypeSpec targetClass) {
        return JavaFile.builder(packageName, targetClass)
                       .addFileComment("This class is generated by Neanderthal. Do not modify!")
                       .build();
    }

    private static Class getClassForDataType(String type) {
        Class klass;
        switch (type) {
        case "int":
            klass = int.class;
            break;
        case "float":
            klass = float.class;
            break;
        case "double":
            klass = double.class;
            break;
        case "short":
            klass = short.class;
            break;
        case "long":
            klass = long.class;
            break;
        case "boolean":
            klass = boolean.class;
            break;
        case "String":
            klass = String.class;
            break;
        case "CharSequence":
            klass = CharSequence.class;
            break;
        default:
            klass = null;
        }
        return klass;
    }

    private static String getCastMethod(String className) {
        String castMethod;
        switch (className) {
        case "int":
            castMethod = "getInt";
            break;
        case "float":
            castMethod = "getFloat";
            break;
        case "double":
            castMethod = "getDouble";
            break;
        case "short":
            castMethod = "getShort";
            break;
        case "long":
            castMethod = "getLong";
            break;
        case "boolean":
            castMethod = "getBoolean";
            break;
        case "String":
            castMethod = "getString";
            break;
        case "CharSequence":
            castMethod = "getString";
            break;
        default:
            castMethod = null;
        }
        return castMethod;
    }
}

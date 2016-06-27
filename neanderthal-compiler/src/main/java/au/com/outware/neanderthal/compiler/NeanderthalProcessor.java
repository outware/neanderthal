package au.com.outware.neanderthal.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.sun.javafx.binding.StringFormatter;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import au.com.outware.neanderthal.annotation.NeanderthalApp;
import au.com.outware.neanderthal.annotation.NeanderthalConfiguration;
import au.com.outware.neanderthal.annotation.Property;

import static javax.tools.Diagnostic.Kind.ERROR;

/**
 * @author huannguyen
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({"au.com.outware.neanderthal.annotation.NeanderthalConfiguration", "au.com.outware.neanderthal.annotation.Property", "au.com.outware.neanderthal.annotation.NeanderthalApp"})
public class NeanderthalProcessor extends AbstractProcessor {

    private boolean classesGenerated;
    private Elements elementUtils;
    private Filer filer;
    private static List<String> mValidDataTypes = Arrays.asList("int", "double", "float", "short", "long", "boolean", "String", "CharSequence");

    @Override public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        elementUtils = env.getElementUtils();
        filer = env.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> configElements = roundEnv.getElementsAnnotatedWith(NeanderthalConfiguration.class);

        if(configElements == null || configElements.size() == 0) {
            if(!classesGenerated) {
                throw new RuntimeException(String.format("No Configuration class found! A configuration class must be annotated with a '%s' annotation", NeanderthalConfiguration.class.getSimpleName()));
            } else {
                return true;
            }
        }

        if(configElements.size() > 1) {
            throw new RuntimeException(StringFormatter
                    .format("Only one class should be annotated with a 'NeanderthalConfiguration' annotation. %d found", configElements.size()).getValue());
        }

        // we know for sure an element with a 'NeanderthalConfiguration' annotation is a class
        TypeElement configElement = (TypeElement) configElements.iterator().next();

        // search for 'NeanderthalApp'
        Set<? extends Element> appElements = roundEnv.getElementsAnnotatedWith(NeanderthalApp.class);

        if(appElements == null || appElements.size() == 0) {
            if(!classesGenerated) {
                throw new RuntimeException(String.format("No class annotated with a '%s' annotation found", NeanderthalApp.class.getSimpleName()));
            } else {
                return true;
            }
        }

        if(appElements.size() > 1) {
            throw new RuntimeException(StringFormatter.format("Only one class should be annotated with a '%s' annotation. %d found", NeanderthalApp.class.getSimpleName(), appElements.size()).getValue());
        }

        // getting package name of application class to generate config manager locator
        TypeElement appElement = (TypeElement) appElements.iterator().next();
        String appPackageName = getPackageName(appElement);

        // getting data for config manager generation
        String configManagerPackageName = getPackageName(configElement);
        ClassName configurationClassName = ClassName.get(configManagerPackageName, configElement.getSimpleName().toString());

        // obtain configuration properties and their display names and data types
        Map<String, String> propertyDisplayNames = new LinkedHashMap<>();
        Map<String, String> propertyDataTypes = new LinkedHashMap<>();
        boolean hasProperty = false;

        for(Element enclosedElement: configElement.getEnclosedElements()) {
            Property propertyAnnotation = enclosedElement.getAnnotation(Property.class);
            if(propertyAnnotation != null) {
                String propertyName = enclosedElement.getSimpleName().toString();
                String dataType = getShortTypeName(enclosedElement.asType().toString());
                if(!mValidDataTypes.contains(dataType)) {
                    error(enclosedElement, "Invalid data type found: %s. Only int, double, boolean, String, and CharSequence are accepted.", dataType);
                } else {
                    propertyDataTypes.put(propertyName, dataType);
                    if(propertyAnnotation.value() != null) {
                        propertyDisplayNames.put(propertyName, propertyAnnotation.value());
                    }
                }
                if(!hasProperty) {
                    hasProperty = true;
                }
            }
        }

        // throw error if no property has been found
        if(!hasProperty) {
            error(configElement, String.format("No property found in the configuration class. A property must have a %s annotation", Property.class.getSimpleName()));
        }

        // generate java files
        JavaFile configManagerFile = NeanderthalCodeGenerator.generateConfigManager(configurationClassName, propertyDisplayNames, propertyDataTypes);
        JavaFile locatorFile = NeanderthalCodeGenerator.generateConfigManagerLocator(appPackageName, configManagerPackageName);

        try {
            configManagerFile.writeTo(filer);
            locatorFile.writeTo(filer);
        } catch (IOException e) {
            error(configElement, "Unable to create Config Manager: %s", e.getMessage());
        }

        classesGenerated = true;
        return true;
    }

    private String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    private String getShortTypeName(String type) {
        int index = type.lastIndexOf(".");
        return type.substring(index + 1);
    }

    /**
     * Print error to console
     */
    private void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        processingEnv.getMessager().printMessage(ERROR, message, element);
    }
}

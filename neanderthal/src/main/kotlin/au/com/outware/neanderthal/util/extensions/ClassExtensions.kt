package au.com.outware.neanderthal.util.extensions

import java.lang.reflect.Field
import java.lang.reflect.Modifier

/**
 * @author timmutton
 */
val Class<out Any>.serializableFields: List<Field>
    get() {
        val fields = this.declaredFields.filter { field ->
            !Modifier.isPrivate(field.modifiers) && !Modifier.isTransient(field.modifiers)
        }
        for(field in fields) {
            field.isAccessible = true
        }
        return fields
    }
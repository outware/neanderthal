package au.com.outware.neanderthal.util

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/**
 * @author timmutton
 */
class CharSequenceDeserializer : JsonDeserializer<CharSequence> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): CharSequence? {
        return json?.asString;
    }
}
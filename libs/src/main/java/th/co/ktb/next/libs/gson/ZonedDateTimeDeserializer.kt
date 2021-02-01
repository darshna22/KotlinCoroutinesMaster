package th.co.ktb.next.libs.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeParseException
import java.lang.reflect.Type

class ZonedDateTimeDeserializer : JsonDeserializer<ZonedDateTime> {
  override fun deserialize(
    json: JsonElement,
    typeOfT: Type,
    context: JsonDeserializationContext
  ): ZonedDateTime {
    val primitive = json.asJsonPrimitive

    if (!primitive.isString) {
      throw IllegalArgumentException("Unable to parse ZonedDateTime for '${primitive.asString}'")
    }

    return try {
      ZonedDateTime.parse(primitive.asString, DateTimeFormatter.ISO_ZONED_DATE_TIME)
    } catch (e: DateTimeParseException) {
      ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault())
    }
  }
}

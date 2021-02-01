package th.co.ktb.next.libs.gson

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit
import java.lang.reflect.Type

class ZonedDateTimeSerializer : JsonSerializer<ZonedDateTime> {
  override fun serialize(
    src: ZonedDateTime,
    typeOfSrc: Type,
    context: JsonSerializationContext
  ): JsonElement {
    val fmt = DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(ZoneOffset.UTC)
    return JsonPrimitive(fmt.format(src.truncatedTo(ChronoUnit.SECONDS)))
  }
}

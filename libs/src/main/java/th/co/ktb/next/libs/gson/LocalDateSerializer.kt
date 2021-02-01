package th.co.ktb.next.libs.gson

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.lang.reflect.Type

class LocalDateSerializer : JsonSerializer<LocalDate> {

  override fun serialize(
    src: LocalDate,
    typeOfSrc: Type,
    context: JsonSerializationContext?
  ): JsonElement {
    val fmt = DateTimeFormatter.BASIC_ISO_DATE
    return JsonPrimitive(fmt.format(src))
  }

}

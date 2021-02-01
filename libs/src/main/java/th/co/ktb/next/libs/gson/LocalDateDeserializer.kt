package th.co.ktb.next.libs.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class LocalDateDeserializer : JsonDeserializer<LocalDate> {

  @Suppress("FoldInitializerAndIfToElvis")
  override fun deserialize(
    json: JsonElement,
    typeOfT: Type,
    context: JsonDeserializationContext
  ): LocalDate? {
    val primitive = json.asJsonPrimitive

    if (!primitive.isString) {
      throw IllegalArgumentException("Unable to parse LocalDate for '${primitive.asString}'")
    }
    if (primitive.asString.isNullOrEmpty()) {
      return null
    }
    if (primitive.asString == "0000-00-00") {
      return null
    }

    val date = ALLOWED_FORMATS.fold(null as LocalDate?) { acc, format ->
      return@fold acc ?: try {
        LocalDate.parse(primitive.asString, format)
      } catch (e: DateTimeParseException) {
        null
      }
    }

    if (date == null) {
      throw IllegalArgumentException("Unable to parse LocalDate for '${primitive.asString}'")
    }

    return date
  }

  companion object {
    private val ALLOWED_FORMATS = listOf(
        DateTimeFormatter.BASIC_ISO_DATE,
        DateTimeFormatter.ISO_ZONED_DATE_TIME,
        DateTimeFormatter.ISO_DATE,
        DateTimeFormatter.ofPattern("ddMMyyyy")
    )
  }

}

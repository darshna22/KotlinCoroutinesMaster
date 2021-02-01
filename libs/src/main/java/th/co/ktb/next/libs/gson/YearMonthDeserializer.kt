package th.co.ktb.next.libs.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeParseException
import java.lang.reflect.Type

class YearMonthDeserializer : JsonDeserializer<YearMonth> {

  @Suppress("FoldInitializerAndIfToElvis")
  override fun deserialize(
    json: JsonElement, typeOfT: Type, context: JsonDeserializationContext
  ): YearMonth {
    val primitive = json.asJsonPrimitive

    if (!primitive.isString) {
      throw IllegalArgumentException("Unable to parse YearMonth for '${primitive.asString}'")
    }

    val date = ALLOWED_FORMATS.fold(null as YearMonth?) { acc, format ->
      return@fold acc ?: try {
        YearMonth.parse(primitive.asString, format)
      } catch (e: DateTimeParseException) {
        null
      }
    }

    if (date == null) {
      throw IllegalArgumentException("Unable to parse YearMonth for '${primitive.asString}'")
    }

    return date
  }

  companion object {
    private val ALLOWED_FORMATS = listOf(
        DateTimeFormatter.ofPattern("yyyy-MM"),
        DateTimeFormatter.BASIC_ISO_DATE,
        DateTimeFormatter.ISO_ZONED_DATE_TIME,
        DateTimeFormatter.ISO_DATE
    )
  }

}

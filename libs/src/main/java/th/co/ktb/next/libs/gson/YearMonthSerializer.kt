package th.co.ktb.next.libs.gson

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import org.threeten.bp.MonthDay
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import java.lang.reflect.Type

class YearMonthSerializer : JsonSerializer<YearMonth> {

  override fun serialize(
    src: YearMonth,
    typeOfSrc: Type,
    context: JsonSerializationContext
  ): JsonElement {
    val fmt = DateTimeFormatter.ofPattern("yyyy-MM")
    return JsonPrimitive(fmt.format(src))
  }

}

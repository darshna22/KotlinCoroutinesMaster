package th.co.ktb.next.libs.i18n.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.threeten.bp.ZonedDateTime

data class LanguagePack(
  @SerializedName("versionNumber") val version: String,
  @SerializedName("languagePackLastModified") val lastModified: ZonedDateTime,
  @SerializedName("content") val translations: Map<String, Map<String, String>>
) {

  /**
   * Returns the language data of a particular language, or null if not available.
   */
  fun forLanguage(lang: String) = translations[lang]

  /**
   * Returns the language data as JSON of a particular language, or null if not available.
   */
  fun forLanguageAsJson(lang: String, gson: Gson): String? {
    val data = this.forLanguage(lang) ?: return null
    return gson.toJson(data)
  }

}

package th.co.ktb.next.libs.i18n.models

import io.reactivex.rxjava3.functions.Supplier
import th.co.ktb.next.libs.i18n.T

enum class SupportedLanguage {

  ENGLISH {
    override var iso639_1 = "en"
    override var iso639_2 = "eng"
    override var locale = "en-TH"
    override var displayName = Supplier { T.get("common_language_selection_english") }
  },

  INDONESIAN {
    override var iso639_1 = "id"
    override var iso639_2 = "ind"
    override var locale = ""
    override var displayName = Supplier { "Indonesian" }
  },

  MALAY {
    override var iso639_1 = "ms"
    override var iso639_2 = "may"
    override var locale = ""
    override var displayName = Supplier { "Bahasa Malaysia" }
  },

  THAI {
    override var iso639_1 = "th"
    override var iso639_2 = "tha"
    override var locale = "th-TH"
    override var displayName = Supplier { T.get("common_language_selection_thai") }
  };

  abstract var iso639_1: String
  abstract var iso639_2: String
  abstract var locale: String
  abstract var displayName: Supplier<String>

  /**
   * Returns the default string representation of this language (ISO639-1).
   */
  override fun toString() = iso639_1

  companion object {
    /**
     * Converts a ISO639-1 language to [SupportedLanguage]. Returns `null` if
     * the language is not supported.
     */
    fun fromIso639_1(lang: String): SupportedLanguage? {
      return values().find { it.iso639_1 == lang }
    }

    /**
     * Converts a ISO639-2 language to [SupportedLanguage]. Returns `null` if
     * the language is not supported.
     */
    fun fromIso639_2(lang: String): SupportedLanguage? {
      return values().find { it.iso639_2 == lang }
    }
  }
}

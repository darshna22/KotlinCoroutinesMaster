package th.co.ktb.next.libs.i18n

import android.app.Application
import com.i18next.android.Operation
import th.co.ktb.next.libs.i18n.models.SupportedLanguage
import java.util.*

/**
 * [T] is a static class for easily accessible localized strings.
 *
 * It must first be initialized with an instance of [LanguagePackProvider].
 * This should be done on [Application.onCreate].
 */
object T {

  private lateinit var repository: LanguagePackProvider
  private lateinit var defaultLanguage: SupportedLanguage

  /**
   * Returns the localized string of the given key. Returns the key itself if a localized string is
   * not found.
   */
  fun get(key: String, operation: Operation? = null): String =
      repository.get(key, operation) ?: key

  /**
   * Returns the localized string of the given key and language. Returns the key itself if a
   * localized string is not found.
   */
  fun get(key: String, lang: SupportedLanguage, operation: Operation? = null): String =
      repository.get(key, lang, operation) ?: key

  /**
   * Sets the current localization language to use.
   */
  fun setLanguage(lang: SupportedLanguage) = repository.setLanguage(lang)

  /**
   * Get the current localization language
   */
  fun getLanguage(): SupportedLanguage {
    return repository.getLanguage()
  }

  /**
   * Get the current Locale
   */
  fun getLocale() = Locale(getLanguage().iso639_1)

  /**
   * @see LanguagePackRepository.checkForUpdates
   */
  fun checkForUpdates() = repository.checkForUpdates()

  /**
   * @see LanguagePackRepository.load
   */
  fun reload(defaultLanguage: SupportedLanguage) = repository.load(defaultLanguage)

  /**
   * Sets the repository to use.
   */
  fun setRepository(
    repository: LanguagePackProvider, defaultLanguage: SupportedLanguage
  ) {
    T.repository = repository
    T.defaultLanguage = defaultLanguage
    reload(defaultLanguage)
  }

}

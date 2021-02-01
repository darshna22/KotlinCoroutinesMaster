package th.co.ktb.next.libs.i18n

import th.co.ktb.next.libs.i18n.models.SupportedLanguage
import com.i18next.android.Operation
import io.reactivex.rxjava3.core.Single

interface LanguagePackProvider {

  fun get(key: String, operation: Operation? = null): String?

  fun get(key: String, language: SupportedLanguage, operation: Operation? = null): String?

  fun setLanguage(language: SupportedLanguage)

  fun getLanguage(): SupportedLanguage

  fun checkForUpdates(): Single<Boolean>

  fun load(defaultLanguage: SupportedLanguage)

}

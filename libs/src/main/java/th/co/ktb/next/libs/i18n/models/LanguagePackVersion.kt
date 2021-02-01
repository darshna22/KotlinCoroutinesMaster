package th.co.ktb.next.libs.i18n.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class LanguagePackVersion(
  @SerializedName("lastModified") val lastModified: Date
)

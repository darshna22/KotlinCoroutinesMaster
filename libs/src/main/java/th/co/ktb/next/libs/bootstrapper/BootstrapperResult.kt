package th.co.ktb.next.libs.bootstrapper

class BootstrapperResult : HashMap<String, Any>() {

  fun putString(key: String, value: String) {
    this[key] = value
  }

  fun getString(key: String): String? {
    return this[key] as? String
  }

  fun putInt(key: String, value: Int) {
    this[key] = value
  }

  fun getInt(key: String): Int? {
    return this[key] as? Int
  }

  fun putBoolean(key: String, value: Boolean) {
    this[key] = value
  }

  fun getBoolean(key: String): Boolean? {
    return this[key] as? Boolean
  }

  fun putObject(key: String, value: Any) {
    this[key] = value
  }

  fun getObject(key: String): Any? {
    return this[key]
  }

}

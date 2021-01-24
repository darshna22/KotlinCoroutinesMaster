package com.example.kotlincorotines.rx

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken

/**
 * Creates a [ViewModel] bound to this activity using the instance returned
 * from the [factory] function.
 *
 * If the [factory] function obtains its instance from a Dagger component, this
 * will allow you to perform constructor injection.
 *
 * See [Stack Overflow](https://stackoverflow.com/questions/50673266).
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified VM : ViewModel> AppCompatActivity.bindViewModel(

  /** A factory method that returns [VM]. */
  crossinline factory: () -> VM

): VM {
  val vmClass = VM::class.java

  // Create a custom view model factory on the fly that returns [VM].
  val viewModelProviderFactory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
      if (modelClass != vmClass) {
        throw IllegalArgumentException("Unexpected argument $modelClass")
      }
      return factory.invoke() as T
    }
  }

  return ViewModelProviders.of(this, viewModelProviderFactory).get(vmClass)
}

/**
 * @see PermissionUtils.checkPermissions
 */
fun AppCompatActivity.checkPermissions(
  permissions: List<String>,
  onDenied: ((MultiplePermissionsReport) -> Unit)? = null,
  onRationaleRequired: ((PermissionToken) -> Unit)? = null,
  onGranted: () -> Unit
) {
  PermissionUtils.checkPermissions(
      activity = this,
      permissions = permissions,
      onDenied = onDenied,
      onRationaleRequired = onRationaleRequired,
      onGranted = onGranted
  )
}

/**
 * @see PermissionUtils.checkPermission
 */
fun AppCompatActivity.checkPermission(
  permission: String,
  onDenied: ((MultiplePermissionsReport) -> Unit)? = null,
  onRationaleRequired: ((PermissionToken) -> Unit)? = null,
  onGranted: () -> Unit
) {
  PermissionUtils.checkPermission(
      activity = this,
      permission = permission,
      onDenied = onDenied,
      onRationaleRequired = onRationaleRequired,
      onGranted = onGranted
  )
}

/**
 * Launches a deep link [uri]. You may optionally restrict the deep link to
 * the current application via the [internalOnly] flag.
 *
 * If there are no applications that can support the deep link, [onNotSupported]
 * will be invoked.
 */
fun AppCompatActivity.launchDeeplink(
  uri: Uri,
  internalOnly: Boolean = false,
  onNotSupported: (() -> Unit)? = null
) {
  val intent = Intent(Intent.ACTION_VIEW, uri)

  if (internalOnly) {
    intent.setPackage(packageName)
  }

  // Verify that there's an app that can support this deep link.
  if (packageManager.queryIntentActivities(intent, 0).size < 1) {
    onNotSupported?.invoke()
    return
  }

  startActivity(intent)
}

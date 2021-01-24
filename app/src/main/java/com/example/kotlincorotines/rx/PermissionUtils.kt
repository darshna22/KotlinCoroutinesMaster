package com.example.kotlincorotines.rx

import android.app.Activity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

/**
 * [PermissionUtils] is a wrapper around [Dexter] to simplify the process
 * of requesting for permissions.
 */
object PermissionUtils {

  /**
   * Checks for required [permissions].
   *
   * @param onDenied The callback to be invoked when any [permissions] is denied.
   * @param onRationaleRequired The callback to be invoked with a [PermissionToken]
   *   when a rationale is required. It is important that the token is used, otherwise
   *   you will not be able to request for permissions again.
   * @param onGranted The callback to be invoked when all permissions are granted.
   */
  fun checkPermissions(
    activity: Activity,
    permissions: List<String>,
    onDenied: ((MultiplePermissionsReport) -> Unit)? = null,
    onRationaleRequired: ((PermissionToken) -> Unit)? = null,
    onGranted: () -> Unit
  ) {
    Dexter.withActivity(activity)
        .withPermissions(permissions)
        .withListener(object : MultiplePermissionsListener {
          override fun onPermissionsChecked(report: MultiplePermissionsReport) {
            if (report.areAllPermissionsGranted()) {
              onGranted.invoke()
            } else {
              onDenied?.invoke(report)
            }
          }

          override fun onPermissionRationaleShouldBeShown(
            permissions: MutableList<PermissionRequest>,
            token: PermissionToken
          ) {
            if (onRationaleRequired != null) {
              onRationaleRequired.invoke(token)
            } else {
              token.continuePermissionRequest()
            }
          }
        })
        .check()
  }

  /**
   * Checks for required [permission].
   *
   * @param onDenied The callback to be invoked when the [permission] is denied.
   * @param onRationaleRequired The callback to be invoked with a [PermissionToken]
   *   when a rationale is required. It is important that the token is used, otherwise
   *   you will not be able to request for permissions again.
   * @param onGranted The callback to be invoked when all permissions are granted.
   */
  fun checkPermission(
    activity: Activity,
    permission: String,
    onDenied: ((MultiplePermissionsReport) -> Unit)? = null,
    onRationaleRequired: ((PermissionToken) -> Unit)? = null,
    onGranted: () -> Unit
  ) {
    checkPermissions(
        activity = activity,
        permissions = listOf(permission),
        onDenied = onDenied,
        onRationaleRequired = onRationaleRequired,
        onGranted = onGranted
    )
  }

}

package com.example.kotlincorotines.rx

import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken

class PermissionRequest(
  val permissions: List<String>,
  val onDenied: ((MultiplePermissionsReport) -> Unit)? = null,
  val onRationaleRequired: ((PermissionToken) -> Unit)? = null,
  val onGranted: () -> Unit
)
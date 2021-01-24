package com.example.kotlincorotines.rx

enum class ErrorDisplayType(val value: String) {
  CUSTOM_POPUP("Custom Popup"),
  CALLOUT("Callout"),
  CUSTOM_ERROR_COMPONENT("Custom Error Component"),
  INLINE("Inline Validation"),
  NATIVE_ALERT("Native Alert"),
  FULL_SCREEN("Full Screen Error"),
}

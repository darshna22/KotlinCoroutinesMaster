package com.example.kotlincorotines.rx

interface TokenRefreshUiHandler {

  fun onTokenRefreshFailure(e: TokenRefreshException)

}

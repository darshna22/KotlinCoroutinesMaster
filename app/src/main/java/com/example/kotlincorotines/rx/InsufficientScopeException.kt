package com.example.kotlincorotines.rx

import java.lang.RuntimeException

class InsufficientScopeException(val requiredScope: NextScope) : RuntimeException()

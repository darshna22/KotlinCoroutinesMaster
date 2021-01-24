package com.example.kotlincorotines.rx

interface PrivilegeEscalationHandler {

  /**
   * Request for a privilege escalation using [request].
   */
  fun requestPrivilege(request: PrivilegeEscalationRequest)

}

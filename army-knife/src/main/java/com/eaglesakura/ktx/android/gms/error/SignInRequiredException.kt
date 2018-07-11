package com.eaglesakura.ktx.android.gms.error

import com.google.android.gms.common.ConnectionResult

/**
 * Required sign-in
 */
class SignInRequiredException(connectionResult: ConnectionResult) : SignInException(connectionResult)
package com.eaglesakura.armyknife.android.extensions

import com.eaglesakura.armyknife.android.gms.error.FirebaseAuthFailedException
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.experimental.delay
import java.util.concurrent.TimeUnit

/**
 * Firebase auth token with Cached.
 */
private var tokenCache: String? = null

/**
 * Expire time, milli seconds.
 */
private var tokenExpireTime: Long = 0

/**
 * Get Firebase-access-token from GooglePlayService with Managed cache.
 * When after than expired time, This method refresh to tokens.
 */
suspend fun FirebaseAuth.getCachedAccessToken(): String {
    if (!tokenCache.isNullOrEmpty() && System.currentTimeMillis() < tokenExpireTime) {
        // トークンがまだ有効である
        return tokenCache!!
    }

    // トークンをリフレッシュする
    val user = currentUser ?: throw FirebaseAuthFailedException("not authorized")
    val tokenTask = user.getIdToken(true).awaitWithSuspend()
    if (!tokenTask.isSuccessful) {
        throw FirebaseAuthFailedException("getIdToken(true) failed")
    }

    tokenCache = tokenTask.result!!.token
    tokenExpireTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(59)
    return tokenCache!!
}

suspend fun FirebaseAuth.awaitLogin(): FirebaseUser {
    while (currentUser == null) {
        delay(1)
    }
    return currentUser!!
}

/**
 * Syntax sugar to Sign-In with Google Account.
 */
fun FirebaseAuth.signIn(account: GoogleSignInAccount) = signInWithCredential(GoogleAuthProvider.getCredential(account.idToken, null))


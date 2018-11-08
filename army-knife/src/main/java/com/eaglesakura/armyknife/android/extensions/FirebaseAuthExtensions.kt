@file:Suppress("unused")

package com.eaglesakura.armyknife.android.extensions

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.delay

/**
 * await Firebase auth.
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
suspend fun FirebaseAuth.awaitLogin(): FirebaseUser {
    while (currentUser == null) {
        delay(1)
    }
    return currentUser!!
}

/**
 * Syntax sugar to Sign-In with Google Account.
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
fun FirebaseAuth.signIn(account: GoogleSignInAccount) =
    signInWithCredential(GoogleAuthProvider.getCredential(account.idToken, null))

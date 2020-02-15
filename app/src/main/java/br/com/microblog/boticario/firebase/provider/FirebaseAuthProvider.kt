package br.com.microblog.boticario.firebase.provider

import android.app.Activity
import br.com.microblog.boticario.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

interface FirebaseAuthProvider {

    fun getAuthInstance(): FirebaseAuth
    fun createUserWithEmailAndPassword(user: User, activity: Activity, createUserListener: CreateUserListener)
    fun sendPasswordResetEmail(email: String, passwordResetEmailListener: PasswordResetEmailListener)
    fun signInWithEmailAndPassword(user: User, signInWithEmailAndPasswordListener: SignInWithEmailAndPasswordListener)
}

open class CreateUserListener (
    val onComplete: ((Task<AuthResult>) -> Unit)? = null
)

open class PasswordResetEmailListener (
    val onComplete: ((Task<Void>) -> Unit)?
)

open class SignInWithEmailAndPasswordListener (
    val onComplete: ((Task<AuthResult>) -> Unit)?
)
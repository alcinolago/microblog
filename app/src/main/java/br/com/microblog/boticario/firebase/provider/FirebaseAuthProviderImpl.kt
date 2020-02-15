package br.com.microblog.boticario.firebase.provider

import android.app.Activity
import br.com.microblog.boticario.model.User
import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthProviderImpl : FirebaseAuthProvider {

    private var auth: FirebaseAuth? = null

    override fun getAuthInstance(): FirebaseAuth {
        if (auth == null) {
            auth = FirebaseAuth.getInstance()
        }

        return auth as FirebaseAuth
    }

    override fun createUserWithEmailAndPassword(
        user: User,
        activity: Activity,
        createUserListener: CreateUserListener
    ) {

        getAuthInstance().createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener(activity) { task ->
                createUserListener.onComplete?.invoke(task)
            }
    }

    override fun sendPasswordResetEmail(
        email: String,
        passwordResetEmailListener: PasswordResetEmailListener
    ) {

        getAuthInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                passwordResetEmailListener.onComplete?.invoke(task)
            }
    }

    override fun signInWithEmailAndPassword(
        user: User,
        signInWithEmailAndPasswordListener: SignInWithEmailAndPasswordListener
    ) {
        getAuthInstance().signInWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                signInWithEmailAndPasswordListener.onComplete?.invoke(task)
            }
    }
}

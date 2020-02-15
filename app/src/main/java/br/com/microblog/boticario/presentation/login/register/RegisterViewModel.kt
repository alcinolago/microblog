package br.com.microblog.boticario.presentation.login.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.microblog.boticario.R
import br.com.microblog.boticario.extension.Event
import br.com.microblog.boticario.extension.mutableLiveData
import br.com.microblog.boticario.firebase.provider.FirebaseDataProvider
import br.com.microblog.boticario.model.User
import br.com.microblog.boticario.model.UserData
import br.com.microblog.boticario.provider.StringProvider


class RegisterViewModel(
    private val stringProvider: StringProvider,
    private val db: FirebaseDataProvider
    ) : ViewModel() {

        var user = User()
        var userData = UserData()
        val FIRESTORE_USERS = "users"
        val nameErrorMessage = MutableLiveData<Event<String>>()
        val emailErrorMessage = MutableLiveData<Event<String>>()
        val passwordErrorMessage = MutableLiveData<Event<String>>()
        val isHideKeyBoard = mutableLiveData(Event(false))
        val createUser = MutableLiveData<User>()
        val isUserSaved = mutableLiveData(Event(false))
        val errorSaveUser = MutableLiveData<Event<String>>()

        fun validateDataEntry() {

            isHideKeyBoard.value = Event(true)

            if (user.name.trim().isEmpty()) {
                nameErrorMessage.value = Event(stringProvider.getString(R.string.error_input_name))
                return
            }

            if (user.email.trim().isEmpty()) {
                emailErrorMessage.value = Event(stringProvider.getString(R.string.error_input_login))
                return
            }

            if (user.password.trim().isEmpty()) {
                passwordErrorMessage.value = Event(stringProvider.getString(R.string.error_input_password))
                return
            }

            if (user.email.isNotEmpty() && user.password.isNotEmpty())
                createUser()
        }

        private fun createUser() {
            createUser.value = user
            user = User()
        }

        fun saveUser(user: User, currentUser: String) {

            userData.id = currentUser
            userData.name = user.name
            userData.email = user.email

            db.getDatabaseReference()
                .collection(FIRESTORE_USERS)
                .document(currentUser)
                .set(userData)
                .addOnSuccessListener {
                    isUserSaved.value = Event(true)
                }
                .addOnFailureListener { exception ->
                    errorSaveUser.value = Event(exception.message!!)
                }
        }
    }

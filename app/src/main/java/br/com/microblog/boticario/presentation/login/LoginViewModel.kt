package br.com.microblog.boticario.presentation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.microblog.boticario.R
import br.com.microblog.boticario.extension.Event
import br.com.microblog.boticario.extension.mutableLiveData
import br.com.microblog.boticario.model.User
import br.com.microblog.boticario.provider.StringProvider

class LoginViewModel(private val stringProvider: StringProvider) : ViewModel() {

    var user = User()
    val signIn = MutableLiveData<User>()
    val emailErrorMessage = MutableLiveData<Event<String>>()
    val passwordErrorMessage = MutableLiveData<Event<String>>()
    val isHideKeyBoard = mutableLiveData(Event(false))
    val navToForgetPassword = mutableLiveData(Event(false))
    val navToRegister = mutableLiveData(Event(false))
    val navToPautas = mutableLiveData(Event(false))

    fun validateDataEntry() {

        isHideKeyBoard.value = Event(true)

        if (user.email.trim().isEmpty()) {
            emailErrorMessage.value = Event(stringProvider.getString(R.string.error_input_login))
            return
        }

        if (user.password.trim().isEmpty()) {
            passwordErrorMessage.value =
                Event(stringProvider.getString(R.string.error_input_password))
            return
        }

        if (user.email.isNotEmpty() && user.password.isNotEmpty())
            doLogin()
    }

    private fun doLogin() {
        signIn.value = user
        user = User()
    }

    fun forgotPassword() {
        navToForgetPassword.value = Event(true)
    }

    fun registeuser() {
        navToRegister.value = Event(true)
    }

    fun navToPautas() {
        navToPautas.value = Event(true)
    }
}
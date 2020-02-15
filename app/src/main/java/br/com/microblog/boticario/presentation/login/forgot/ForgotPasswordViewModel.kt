package br.com.microblog.boticario.presentation.login.forgot

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.microblog.boticario.R
import br.com.microblog.boticario.extension.Event
import br.com.microblog.boticario.extension.mutableLiveData
import br.com.microblog.boticario.provider.StringProvider

class ForgotPasswordViewModel(private val stringProvider: StringProvider) : ViewModel() {

    val email = MutableLiveData<String>()
    val emailErrorMessage = MutableLiveData<Event<String>>()
    val isHideKeyBoard = mutableLiveData(Event(false))
    val sendPasswordResetEmail = MutableLiveData<String>()

    fun validateDataEntry() {

        val emailInput = email.value

        isHideKeyBoard.value = Event(true)

        if (emailInput?.trim().isNullOrEmpty()) {
            emailErrorMessage.value = Event(stringProvider.getString(R.string.error_input_login))
        } else {
            sendPasswordResetEmail()
        }
    }

    private fun sendPasswordResetEmail() {
        sendPasswordResetEmail.value = email.value
        email.value = ""
    }
}

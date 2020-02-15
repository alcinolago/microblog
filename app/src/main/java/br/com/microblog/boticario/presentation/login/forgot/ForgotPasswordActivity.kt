package br.com.microblog.boticario.presentation.login.forgot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.com.microblog.boticario.Constants
import br.com.microblog.boticario.R
import br.com.microblog.boticario.databinding.ActivityForgotPasswordBinding
import br.com.microblog.boticario.firebase.provider.FirebaseAuthProvider
import br.com.microblog.boticario.firebase.provider.PasswordResetEmailListener
import br.com.microblog.boticario.helper.DialogProgressBar
import br.com.microblog.boticario.helper.InputTextWatcher
import br.com.microblog.boticario.helper.Keyboard
import kotlinx.android.synthetic.main.activity_forgot_password.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForgotPasswordActivity : AppCompatActivity() {

    private val viewModel by viewModel<ForgotPasswordViewModel>()
    private val mAuth: FirebaseAuthProvider by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivityForgotPasswordBinding>(this, R.layout.activity_forgot_password)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initViews()
        setupToolbar(Constants.TOOLBAR_FORGET_PASSWORD)
        observerValidateDataEntry()
        observeSendPasswordResetEmail()
        observerIsHideKeyboard()
    }

    fun initViews() {
        editTextEmail!!.addTextChangedListener(InputTextWatcher(input_layout_email!!))
    }

    fun setupToolbar(title: String) {

        setSupportActionBar(toolbar)
        supportActionBar?.title = title
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun observerValidateDataEntry() {

        viewModel.emailErrorMessage.observe(this, Observer {
            it?.getContentIfNotHandled()?.let { errorMessage ->
                input_layout_email.error = errorMessage
                editTextEmail.requestFocus()
            }
        })
    }

    fun observerIsHideKeyboard(){
        viewModel.isHideKeyBoard.observe(this, Observer {
            it?.getContentIfNotHandled()?.let{ isHideKeyBoard ->
                if(isHideKeyBoard) Keyboard.hide(this)
            }
        })
    }

    fun observeSendPasswordResetEmail() {

        viewModel.sendPasswordResetEmail.observe(this, Observer {
            it?.let { email ->

                DialogProgressBar.show(this)

                mAuth.sendPasswordResetEmail(email, PasswordResetEmailListener(onComplete = { task ->
                    if (task.isSuccessful) {
                        DialogProgressBar.dismiss()
                        finish()
                    } else {
                        DialogProgressBar.dismiss()
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }))
            }
        })
    }
}

package br.com.microblog.boticario.presentation.login.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.microblog.boticario.R
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.com.microblog.boticario.Constants
import br.com.microblog.boticario.databinding.ActivityRegisterBinding
import br.com.microblog.boticario.firebase.provider.CreateUserListener
import br.com.microblog.boticario.firebase.provider.FirebaseAuthProvider
import br.com.microblog.boticario.helper.DialogProgressBar
import br.com.microblog.boticario.helper.InputTextWatcher
import br.com.microblog.boticario.helper.Keyboard
import kotlinx.android.synthetic.main.activity_register.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private val viewModel by viewModel<RegisterViewModel>()
    private val mAuth: FirebaseAuthProvider by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityRegisterBinding>(this, R.layout.activity_register)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupToolbar(Constants.TOOLBAR_REGISTER_USER)
        initViews()
        observerValidateDataEntry()
        observeCreateUser()
        observeSaveUserSuccess()
        observeSaveUserFailed()
        observerIsHideKeyboard()
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

    fun initViews() {
        editTextName!!.addTextChangedListener(InputTextWatcher(input_layout_name!!))
        editTextEmail!!.addTextChangedListener(InputTextWatcher(input_layout_email!!))
        editTextPassword!!.addTextChangedListener(InputTextWatcher(input_layout_password!!))
    }

    fun observerIsHideKeyboard(){
        viewModel.isHideKeyBoard.observe(this, Observer {
            it?.getContentIfNotHandled()?.let{ isHideKeyBoard ->
                if(isHideKeyBoard) Keyboard.hide(this)
            }
        })
    }

    fun observerValidateDataEntry() {

        viewModel.nameErrorMessage.observe(this, Observer {
            it?.getContentIfNotHandled()?.let { errorMessage ->
                input_layout_name.error = errorMessage
                editTextName.requestFocus()
            }
        })

        viewModel.emailErrorMessage.observe(this, Observer {
            it?.getContentIfNotHandled()?.let { errorMessage ->
                input_layout_email.error = errorMessage
                editTextEmail.requestFocus()
            }
        })

        viewModel.passwordErrorMessage.observe(this, Observer {
            it?.getContentIfNotHandled()?.let { errorMessage ->
                input_layout_password.error = errorMessage
                editTextPassword.requestFocus()
            }
        })
    }

    fun observeCreateUser() {

        viewModel.createUser.observe(this, Observer {
            it?.let { userData ->

                DialogProgressBar.show(this)

                mAuth.createUserWithEmailAndPassword(userData, this, CreateUserListener(onComplete = { task ->
                    if (task.isSuccessful) {
                        val currentUser = mAuth.getAuthInstance().currentUser
                        viewModel.saveUser(userData, currentUser?.uid!!)
                    } else {
                        DialogProgressBar.dismiss()
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }))
            }
        })
    }

    fun observeSaveUserSuccess(){
        viewModel.isUserSaved.observe(this, Observer{
            it?.getContentIfNotHandled()?.let{ isUserSaved ->
                if(isUserSaved) {
                    DialogProgressBar.dismiss()
                    finish()
                }
            }
        })
    }

    fun observeSaveUserFailed(){
        viewModel.errorSaveUser.observe(this, Observer{
            it?.getContentIfNotHandled()?.let{ errorMessage ->
                DialogProgressBar.dismiss()
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}

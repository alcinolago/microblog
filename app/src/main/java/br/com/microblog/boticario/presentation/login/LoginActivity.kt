package br.com.microblog.boticario.presentation.login

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.com.microblog.boticario.BuildConfig
import br.com.microblog.boticario.R
import br.com.microblog.boticario.databinding.ActivityLoginBinding
import br.com.microblog.boticario.firebase.provider.FirebaseAuthProvider
import br.com.microblog.boticario.helper.DialogProgressBar
import br.com.microblog.boticario.helper.InputTextWatcher
import br.com.microblog.boticario.helper.Keyboard
import br.com.microblog.boticario.firebase.provider.SignInWithEmailAndPasswordListener
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModel<LoginViewModel>()
    private val mAuth: FirebaseAuthProvider by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initViews()
        observeIsHideKeyboard()
        observeSignIn()
        observerValidateDataEntry()
        observeNavigateTo()
        observerIsHideKeyboard()
        getVersionInfo()
    }

    override fun onStart() {
        super.onStart()
        val user = mAuth.getAuthInstance().currentUser
        if(user != null){
            viewModel.navToPautas()
        }else{
            Log.d("USER","DESLOGADO")
        }
    }

    fun initViews() {
        editTextEmail!!.addTextChangedListener(InputTextWatcher(input_layout_email!!))
        editTextPassword!!.addTextChangedListener(InputTextWatcher(input_layout_password!!))
    }

    override fun onBackPressed() {}

    private fun observeIsHideKeyboard() {

        viewModel.isHideKeyBoard.observe(this, Observer {
            it?.getContentIfNotHandled()?.let { isHideKeyboard ->
                if (isHideKeyboard) Keyboard.hide(this)
            }
        })
    }

    private fun observerValidateDataEntry() {

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

    fun observerIsHideKeyboard(){
        viewModel.isHideKeyBoard.observe(this, Observer {
            it?.getContentIfNotHandled()?.let{ isHideKeyBoard ->
                if(isHideKeyBoard) Keyboard.hide(this)
            }
        })
    }

    private fun observeSignIn() {
        viewModel.signIn.observe(this, Observer {
            it?.let { signInUser ->
                DialogProgressBar.show(this)
                mAuth.signInWithEmailAndPassword(signInUser, SignInWithEmailAndPasswordListener(onComplete = { task ->
                    if (task.isSuccessful) {
                        Log.d("LOGIN","DEU BOA!!!")
                        DialogProgressBar.dismiss()
                        viewModel.navToPautas()
                    } else {
                        DialogProgressBar.dismiss()
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }))
            }
        })
    }

    fun observeNavigateTo() {

        viewModel.navToForgetPassword.observe(this, Observer {
            it?.getContentIfNotHandled()?.let { navToForgetPassword ->
                //if (navToForgetPassword)
                    //startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
            }
        })

        viewModel.navToRegister.observe(this, Observer {
            it?.getContentIfNotHandled()?.let { navToRegister ->
                //if (navToRegister)
                    //startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        })

        viewModel.navToPautas.observe(this, Observer {
            it?.getContentIfNotHandled()?.let { navToPautas ->
//                if (navToPautas) {
//                    startActivity(Intent(this@LoginActivity, PautaListActivity::class.java))
//                    finish()
//                }
            }
        })
    }

    private fun getVersionInfo() {
        var versionName = ""
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            versionName = packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        val appVersion = BuildConfig.BUILD_TYPE.toUpperCase() + " - " + versionName
        app_version.text = appVersion
    }
}

package br.com.microblog.boticario.presentation.blog.form

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.com.microblog.boticario.Constants
import br.com.microblog.boticario.R
import br.com.microblog.boticario.databinding.ActivityFormBlogBinding
import br.com.microblog.boticario.firebase.provider.FirebaseAuthProvider
import br.com.microblog.boticario.helper.DialogProgressBar
import br.com.microblog.boticario.helper.Keyboard
import kotlinx.android.synthetic.main.activity_form_blog.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FormBlogActivity : AppCompatActivity() {

    private val viewModel by viewModel<FormBlogViewModel>()
    private val mAuth: FirebaseAuthProvider by inject()
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityFormBlogBinding>(
            this,
            R.layout.activity_form_blog
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupToolbar(Constants.TOOLBAR_ADD_POST)
        observeGetProfile()
        observeSavePostSuccess()
        observeSavePostFailed()
        oberverIsLoading()
        observerIsHideKeyboard()
    }

    override fun onStart() {
        super.onStart()
        val user = mAuth.getAuthInstance().currentUser
        if (user != null) {
            userId = user.uid
        } else {
            Log.d("USER", "DESLOGADO")
        }
    }

    override fun onResume() {
        super.onResume()
        DialogProgressBar.show(this)
        viewModel.getProfile(userId)
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

    fun observeGetProfile() {
        viewModel.isGetProfileData.observe(this, Observer {
            it.getContentIfNotHandled().let { isGetProfileData ->
                if (isGetProfileData!!)
                    DialogProgressBar.dismiss()
            }
        })
    }

    fun oberverIsLoading() {
        viewModel.isLoading.observe(this, Observer {
            it.getContentIfNotHandled().let { isLoading ->
                if (isLoading!!) DialogProgressBar.show(this)
            }
        })
    }

    fun observerIsHideKeyboard() {
        viewModel.isHideKeyBoard.observe(this, Observer {
            it.getContentIfNotHandled().let { isHideKeyBoard ->
                if (isHideKeyBoard!!) Keyboard.hide(this)
            }
        })
    }

    fun observeSavePostSuccess() {
        viewModel.isPostSaved.observe(this, Observer {
            it.getContentIfNotHandled().let { isUserSaved ->
                if (isUserSaved!!) {
                    DialogProgressBar.dismiss()
                    finish()
                }
            }
        })
    }

    fun observeSavePostFailed() {
        viewModel.errorSavePost.observe(this, Observer {
            it.getContentIfNotHandled().let { errorMessage ->
                DialogProgressBar.dismiss()
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
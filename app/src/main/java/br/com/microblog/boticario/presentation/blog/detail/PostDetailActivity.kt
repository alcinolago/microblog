package br.com.microblog.boticario.presentation.blog.detail

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.com.microblog.boticario.Constants
import br.com.microblog.boticario.R
import br.com.microblog.boticario.databinding.ActivityPostDetailBinding
import br.com.microblog.boticario.firebase.provider.FirebaseAuthProvider
import br.com.microblog.boticario.helper.DialogProgressBar
import br.com.microblog.boticario.helper.Keyboard
import br.com.microblog.boticario.model.Post
import kotlinx.android.synthetic.main.activity_form_blog.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PostDetailActivity : AppCompatActivity() {

    private val viewModel by viewModel<PostDetailViewModel>()
    private val mAuth: FirebaseAuthProvider by inject()
    private lateinit var userId: String
    private lateinit var post: Post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityPostDetailBinding>(
            this,
            R.layout.activity_post_detail
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupToolbar(Constants.TOOLBAR_UPDATE_POST)
        getIntentDataReceived()
        observeGetProfile()
        observeSavePostSuccess()
        observeSavePostFailed()
        oberverIsLoading()
        observerIsHideKeyboard()
        observeAlertRemovePost()
        observeIsDeleted()
        observeIsUpdatePost()
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

    private fun getIntentDataReceived() {

        val extras = intent.extras
        if (extras != null) {
            post = extras.getParcelable(Constants.POST_BUNDLE)!!
            viewModel.post = post
        }
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

    fun observeAlertRemovePost() {

        viewModel.isAlertRemove.observe(this, Observer {
            it?.getContentIfNotHandled()?.let { isAlertRemove ->
                if (isAlertRemove) {
                    val builder = AlertDialog.Builder(this)

                    with(builder)
                    {
                        setTitle("Apagar post")
                        setMessage("Tem certeza que deseja apagar?")
                        setPositiveButton("Sim") { _: DialogInterface, _: Int ->
                            viewModel.removePost(post.id)
                        }
                        setNegativeButton("Não") { _: DialogInterface, _: Int -> }
                        show()
                    }
                }
            }
        })
    }

    fun observeIsDeleted() {

        viewModel.isPostDeleted.observe(this, Observer {
            it?.getContentIfNotHandled()?.let { isPostDeleted ->
                if (isPostDeleted) {
                    DialogProgressBar.dismiss()
                    Toast.makeText(this, "Post removido!", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        })
    }

    fun observeIsUpdatePost(){
        viewModel.isUpdatePost.observe(this, Observer {
            it?.getContentIfNotHandled()?.let { updatePost ->
                if(updatePost)
                    viewModel.updatePost(post.id)
            }
        })
    }
}


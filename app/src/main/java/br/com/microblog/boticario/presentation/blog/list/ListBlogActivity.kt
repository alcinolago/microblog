package br.com.microblog.boticario.presentation.blog.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.microblog.boticario.Constants
import br.com.microblog.boticario.R
import br.com.microblog.boticario.databinding.ActivityListBlogBinding
import br.com.microblog.boticario.firebase.provider.FirebaseAuthProvider
import br.com.microblog.boticario.helper.DialogProgressBar
import br.com.microblog.boticario.helper.ItemClickListener
import br.com.microblog.boticario.model.Post
import br.com.microblog.boticario.presentation.blog.adapter.FirebaseBlogListAdapter
import br.com.microblog.boticario.presentation.blog.detail.PostDetailActivity
import br.com.microblog.boticario.presentation.blog.form.FormBlogActivity
import br.com.microblog.boticario.presentation.login.LoginActivity
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.activity_list_blog.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListBlogActivity : AppCompatActivity() {

    private val viewModel by viewModel<ListBlogViewModel>()
    private val mAuth: FirebaseAuthProvider by inject()
    private lateinit var blogAdapter: FirebaseBlogListAdapter
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityListBlogBinding>(
            this,
            R.layout.activity_list_blog
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupToolbar(Constants.TOOLBAR_POSTS)
        observeNavToAddPost()
        setRecyclerView()
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
        viewModel.getPosts()
    }

    private fun setupToolbar(title: String) {

        setSupportActionBar(toolbar)
        supportActionBar?.title = title
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.posts_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.logout -> logout()
        }

        return super.onOptionsItemSelected(item)
    }

    fun setRecyclerView() {
        viewModel.queryPautas.observe(this, Observer { query ->
            val options = FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post::class.java)
                .setLifecycleOwner(this)
                .build()

            blogAdapter = FirebaseBlogListAdapter(options, userId, object : ItemClickListener {
                override fun onItemClick(position: Int) {

                    val postIntent = Intent(this@ListBlogActivity, PostDetailActivity::class.java)
                    val bundle = Bundle()
                    val post = options.snapshots[position]
                    bundle.putParcelable(Constants.POST_BUNDLE, post)
                    postIntent.putExtras(bundle)
                    startActivity(postIntent)
                }
            })

            val layout = LinearLayoutManager(
                this,
                RecyclerView.VERTICAL, false
            )
            recyclerViewBlog.setHasFixedSize(true)
            recyclerViewBlog.layoutManager = layout
            recyclerViewBlog.adapter = blogAdapter

            DialogProgressBar.dismiss()
        })
    }

    private fun observeNavToAddPost() {
        viewModel.isNavToAddPost.observe(this, Observer {
            it?.getContentIfNotHandled()?.let { isNavToFormPost ->
                if (isNavToFormPost)
                    startActivity(Intent(this@ListBlogActivity, FormBlogActivity::class.java))
            }
        })
    }

    private fun logout() {
        mAuth.getAuthInstance().signOut()
        startActivity(Intent(this@ListBlogActivity, LoginActivity::class.java))
        finish()
    }
}

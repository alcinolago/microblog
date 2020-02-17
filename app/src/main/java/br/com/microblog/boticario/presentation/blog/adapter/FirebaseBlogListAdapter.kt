package br.com.microblog.boticario.presentation.blog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.microblog.boticario.R
import br.com.microblog.boticario.helper.ItemClickListener
import br.com.microblog.boticario.model.Post
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class FirebaseBlogListAdapter(
    options: FirestoreRecyclerOptions<Post>,
    private val userId: String,
    private val listener: ItemClickListener
) :
    FirestoreRecyclerAdapter<Post, RecyclerView.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FirebaseBlogListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_blog_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, model: Post) {
        (holder as FirebaseBlogListViewHolder).bind(model, userId, position, listener)
    }
}
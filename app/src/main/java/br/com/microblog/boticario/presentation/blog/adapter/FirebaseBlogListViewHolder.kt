package br.com.microblog.boticario.presentation.blog.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import br.com.microblog.boticario.helper.ItemClickListener
import br.com.microblog.boticario.model.Post
import br.com.microblog.boticario.util.TimeUtil
import kotlinx.android.synthetic.main.layout_blog_item.view.*
import java.util.*

class FirebaseBlogListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(posts: Post, userId: String, position: Int, listener: ItemClickListener) {

        itemView.apply {

            if(userId == posts.userId){
                itemView.setOnClickListener { listener.onItemClick(position) }
                itemView.ic_action.visibility = View.VISIBLE
            }
            itemView.author.text = posts.author
            val postDate = posts.postDate.toLong()
            val postDateFormated = TimeUtil.StampToDate(postDate, Locale("pt", "BR"))
            itemView.postDate.text = postDateFormated
            itemView.post.text = posts.post
        }
    }
}
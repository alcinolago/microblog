package br.com.microblog.boticario.presentation.blog.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.microblog.boticario.extension.Event
import br.com.microblog.boticario.firebase.data.FirebaseData
import br.com.microblog.boticario.firebase.provider.FirebaseDataProvider
import com.google.firebase.firestore.Query

class ListBlogViewModel(
    private val db: FirebaseDataProvider,
    private val firebaseData: FirebaseData
) : ViewModel() {

    var queryPautas: MutableLiveData<Query> = MutableLiveData()
    val isNavToAddPost = MutableLiveData<Event<Boolean>>()

    fun getPosts() {
        queryPautas.value = firebaseData.getPosts()
    }

    fun navToAddPost(){
        isNavToAddPost.value = Event(true)
    }
}
package br.com.microblog.boticario.presentation.blog.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.microblog.boticario.Constants
import br.com.microblog.boticario.extension.Event
import br.com.microblog.boticario.extension.mutableLiveData
import br.com.microblog.boticario.firebase.data.FirebaseData
import br.com.microblog.boticario.firebase.provider.FirebaseDataProvider
import br.com.microblog.boticario.model.Post
import br.com.microblog.boticario.model.UserData

class PostDetailViewModel(
    private val firebaseData: FirebaseData,
    private val db: FirebaseDataProvider
) : ViewModel() {

    var post = Post()
    val isGetProfileData = mutableLiveData(Event(false))
    val isHideKeyBoard = mutableLiveData(Event(false))
    val isLoading = mutableLiveData(Event(false))
    val isPostSaved = mutableLiveData(Event(false))
    val isPostDeleted = mutableLiveData(Event(false))
    val errorSavePost = MutableLiveData<Event<String>>()
    val isAlertRemove = mutableLiveData(Event(false))
    val isUpdatePost = mutableLiveData(Event(false))
    val buttonState = MutableLiveData<Boolean>()

    fun getProfile(userId: String) {
        firebaseData.getProfile(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject(UserData::class.java)
                post.author = user?.name.toString()
                post.userId = user?.id.toString()
                post.postDate = System.currentTimeMillis().toString()
                isGetProfileData.value = Event(true)
            }
    }

    private fun checkButtonState() {
        buttonState.value = (post.post.isNotEmpty())
    }

    fun onPostChanged(postText: CharSequence, start: Int, before: Int, count: Int) {
        post.post = postText.toString()
        checkButtonState()
    }

    fun updateAction(){
        isUpdatePost.value = Event(true)
    }

    fun updatePost(postId: String) {

        isHideKeyBoard.value = Event(true)
        isLoading.value = Event(true)

        val postMap: HashMap<String, Any> = HashMap()
        postMap["post"] = post.post

        db.getDatabaseReference()
            .collection(Constants.FIRESTORE_POST)
            .document(postId)
            .update(postMap)
            .addOnSuccessListener {
                isPostSaved.value = Event(true)
            }
            .addOnFailureListener { exception ->
                errorSavePost.value = Event(exception.message!!)
            }
    }

    fun removePost(postId: String){

        isHideKeyBoard.value = Event(true)
        isLoading.value = Event(true)

        db.getDatabaseReference()
            .collection(Constants.FIRESTORE_POST)
            .document(postId)
            .delete()
            .addOnSuccessListener {
                isPostDeleted.value = Event(true)
            }
            .addOnFailureListener { exception ->
                errorSavePost.value = Event(exception.message!!)
            }
    }

    fun alertRemovePost(){
        isAlertRemove.value = Event(true)
    }
}
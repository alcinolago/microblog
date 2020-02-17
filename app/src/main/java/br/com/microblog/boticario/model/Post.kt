package br.com.microblog.boticario.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post (
    var id: String = "",
    var userId: String = "",
    var post: String = "",
    var author: String = "",
    var postDate: String = ""
) : Parcelable
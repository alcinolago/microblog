package br.com.microblog.boticario.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

fun <T : Any> mutableLiveData(initialValue: T? = null): MutableLiveData<T> {
    return MutableLiveData<T>().apply { value = initialValue }
}

fun <T> LiveData<T>.observeNotNull(lifecycleOwner: LifecycleOwner, onChanged: (t: T) -> Unit) {
    this.observe(lifecycleOwner, Observer { if (it != null) onChanged(it) })
}
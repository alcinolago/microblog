package br.com.microblog.boticario.provider

import android.content.Context

class StringProviderImpl(private val context: Context): StringProvider {
    override fun getString(stringID: Int): String {
        return context.getString(stringID)
    }
}
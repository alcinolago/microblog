package br.com.microblog.boticario.provider

interface StringProvider {
    fun getString(stringID: Int): String
}
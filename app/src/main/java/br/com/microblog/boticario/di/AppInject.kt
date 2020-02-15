package br.com.microblog.boticario.di

import br.com.microblog.boticario.firebase.data.FirebaseData
import br.com.microblog.boticario.firebase.data.FirebaseDataImpl
import br.com.microblog.boticario.firebase.provider.FirebaseAuthProvider
import br.com.microblog.boticario.firebase.provider.FirebaseAuthProviderImpl
import br.com.microblog.boticario.firebase.provider.FirebaseDataProvider
import br.com.microblog.boticario.firebase.provider.FirebaseDataProviderImpl
import br.com.microblog.boticario.presentation.login.LoginViewModel
import br.com.microblog.boticario.presentation.login.register.RegisterViewModel
import br.com.microblog.boticario.provider.StringProvider
import br.com.microblog.boticario.provider.StringProviderImpl
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppInject {

    val appInject = module {

        single<StringProvider> { StringProviderImpl(androidContext()) }
        single<FirebaseAuthProvider> { FirebaseAuthProviderImpl() }
        single<FirebaseDataProvider> { FirebaseDataProviderImpl() }
        single<FirebaseData> { FirebaseDataImpl(get()) }

        viewModel { LoginViewModel(get()) }
        viewModel { RegisterViewModel(get(), get()) }
    }
}
package br.com.microblog.boticario.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import br.com.microblog.boticario.extension.Event
import br.com.microblog.boticario.presentation.login.forgot.ForgotPasswordViewModel
import br.com.microblog.boticario.provider.StringProvider
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ForgotPasswordViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var lifecycleOwner: LifecycleOwner

    @Mock
    private lateinit var stringProvider: StringProvider

    @Mock
    private lateinit var observeIsHideKeyBoard: Observer<Event<Boolean>>

    @Mock
    private lateinit var observeEmail: Observer<String>

    @Mock
    private lateinit var observeSendPasswordResetEmail: Observer<String>

    @Mock
    private lateinit var observeEmailErrorMessage: Observer<Event<String>>

    private lateinit var viewModel: ForgotPasswordViewModel
    private lateinit var lifecycle: LifecycleRegistry

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        lifecycle = LifecycleRegistry(lifecycleOwner)
        BDDMockito.given(lifecycleOwner.lifecycle).willReturn(lifecycle)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        viewModel = ForgotPasswordViewModel(stringProvider)
    }

    @Test
    fun `it should initialize fields properly`() {
        assertNull(viewModel.email.value)
        assertNull(viewModel.sendPasswordResetEmail.value)
        assertNull(viewModel.emailErrorMessage.value)
        assertFalse(viewModel.isHideKeyBoard.value?.peekContent()!!)
    }

    @Test
    fun `should return email error message when validate data entry`() {

        viewModel.isHideKeyBoard.observe(lifecycleOwner, observeIsHideKeyBoard)
        viewModel.emailErrorMessage.observe(lifecycleOwner, observeEmailErrorMessage)

        viewModel.validateDataEntry()

        verify(observeIsHideKeyBoard, times(2)).onChanged(any())
        verify(observeEmailErrorMessage, times(1)).onChanged(any())
    }

    @Test
    fun `should call send password reset email when validate data entry`() {

        viewModel.isHideKeyBoard.observe(lifecycleOwner, observeIsHideKeyBoard)
        viewModel.sendPasswordResetEmail.observe(lifecycleOwner, observeSendPasswordResetEmail)
        viewModel.email.observe(lifecycleOwner, observeEmail)

        viewModel.email.value = "teste@teste.com"

        viewModel.validateDataEntry()

        viewModel.email.value = ""

        verify(observeIsHideKeyBoard, times(2)).onChanged(any())
        verify(observeSendPasswordResetEmail, times(1)).onChanged(any())
        verify(observeEmail, times(3)).onChanged(any())
    }
}
package br.com.microblog.boticario.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import br.com.microblog.boticario.extension.Event
import br.com.microblog.boticario.model.User
import br.com.microblog.boticario.presentation.login.LoginViewModel
import br.com.microblog.boticario.provider.StringProvider
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.*
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var lifecycleOwner: LifecycleOwner

    @Mock
    private lateinit var stringProvider: StringProvider

    @Mock
    private lateinit var observeIsHideKeyBoard: Observer<Event<Boolean>>

    @Mock
    private lateinit var observeEventBoolean: Observer<Event<Boolean>>

    @Mock
    private lateinit var observeEmailErrorMessage: Observer<Event<String>>

    @Mock
    private lateinit var observePasswordErrorMessage: Observer<Event<String>>

    @Mock
    private lateinit var observeSignIn: Observer<User>

    private lateinit var viewModel: LoginViewModel
    private lateinit var lifecycle: LifecycleRegistry

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        lifecycle = LifecycleRegistry(lifecycleOwner)
        BDDMockito.given(lifecycleOwner.lifecycle).willReturn(lifecycle)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        viewModel = LoginViewModel(stringProvider)
    }

    @Test
    fun `it should initialize fields properly`() {
        assertNull(viewModel.signIn.value)
        assertNull(viewModel.emailErrorMessage.value)
        assertNull(viewModel.passwordErrorMessage.value)
        assertFalse(viewModel.isHideKeyBoard.value?.peekContent()!!)
        assertFalse(viewModel.navToForgetPassword.value?.peekContent()!!)
        assertFalse(viewModel.navToRegister.value?.peekContent()!!)
        assertFalse(viewModel.navToPautas.value?.peekContent()!!)
    }

    @Test
    fun `should return email error message when validate data entry`(){

        viewModel.isHideKeyBoard.observe(lifecycleOwner, observeIsHideKeyBoard)
        viewModel.emailErrorMessage.observe(lifecycleOwner, observeEmailErrorMessage)
        viewModel.passwordErrorMessage.observe(lifecycleOwner, observePasswordErrorMessage)

        val userWithoutEmail = User()
        userWithoutEmail.email = ""

        viewModel.user = userWithoutEmail

        viewModel.validateDataEntry()

        verify(observeIsHideKeyBoard, times(2)).onChanged(any())
        verify(observeEmailErrorMessage, times(1)).onChanged(any())
        verify(observePasswordErrorMessage, times(0)).onChanged(any())
    }

    @Test
    fun `should return password error message when validate data entry`(){

        viewModel.isHideKeyBoard.observe(lifecycleOwner, observeIsHideKeyBoard)
        viewModel.emailErrorMessage.observe(lifecycleOwner, observeEmailErrorMessage)
        viewModel.passwordErrorMessage.observe(lifecycleOwner, observePasswordErrorMessage)

        val userWithoutPassword = User()
        userWithoutPassword.email = "teste@teste.com"
        userWithoutPassword.password = ""

        viewModel.user = userWithoutPassword

        viewModel.validateDataEntry()

        verify(observeIsHideKeyBoard, times(2)).onChanged(any())
        verify(observeEmailErrorMessage, times(0)).onChanged(any())
        verify(observePasswordErrorMessage, times(1)).onChanged(any())
    }

    @Test
    fun `should call login if email and password are informed when validate data entry`(){

        viewModel.isHideKeyBoard.observe(lifecycleOwner, observeIsHideKeyBoard)
        viewModel.emailErrorMessage.observe(lifecycleOwner, observeEmailErrorMessage)
        viewModel.passwordErrorMessage.observe(lifecycleOwner, observePasswordErrorMessage)
        viewModel.signIn.observe(lifecycleOwner, observeSignIn)

        val userWithEmailPassword = User()
        userWithEmailPassword.email = "teste@teste.com"
        userWithEmailPassword.password = "123456"

        viewModel.user = userWithEmailPassword

        viewModel.validateDataEntry()

        verify(observeIsHideKeyBoard, times(2)).onChanged(any())
        verify(observeEmailErrorMessage, times(0)).onChanged(any())
        verify(observePasswordErrorMessage, times(0)).onChanged(any())
        verify(observeSignIn, times(1)).onChanged(any())
    }

    @Test
    fun `should navigate to forgot password view when call forgot password`(){

        viewModel.navToForgetPassword.observe(lifecycleOwner, observeEventBoolean)

        viewModel.forgotPassword()

        verify(observeEventBoolean, times(2)).onChanged(any())
    }

    @Test
    fun `should navigate to register view when call register`(){

        viewModel.navToRegister.observe(lifecycleOwner, observeEventBoolean)

        viewModel.registeuser()

        verify(observeEventBoolean, times(2)).onChanged(any())
    }

    @Test
    fun `should navigate to pautas list view when call pautas`(){

        viewModel.navToPautas.observe(lifecycleOwner, observeEventBoolean)

        viewModel.navToPautas()

        verify(observeEventBoolean, times(2)).onChanged(any())
    }
}

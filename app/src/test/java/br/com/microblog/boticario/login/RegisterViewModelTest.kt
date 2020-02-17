package br.com.microblog.boticario.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import br.com.microblog.boticario.extension.Event
import br.com.microblog.boticario.firebase.provider.FirebaseDataProvider
import br.com.microblog.boticario.model.User
import br.com.microblog.boticario.presentation.login.register.RegisterViewModel
import br.com.microblog.boticario.provider.StringProvider
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var lifecycleOwner: LifecycleOwner

    @Mock
    private lateinit var stringProvider: StringProvider

    @Mock
    private lateinit var firebaseProvider: FirebaseDataProvider

    @Mock
    private lateinit var observeIsHideKeyBoard: Observer<Event<Boolean>>

    @Mock
    private lateinit var observeNameErrorMessage: Observer<Event<String>>

    @Mock
    private lateinit var observeEmailErrorMessage: Observer<Event<String>>

    @Mock
    private lateinit var observePasswordErrorMessage: Observer<Event<String>>

    @Mock
    private lateinit var observeCreateUser: Observer<User>

    private lateinit var viewModel: RegisterViewModel
    private lateinit var lifecycle: LifecycleRegistry

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        lifecycle = LifecycleRegistry(lifecycleOwner)
        given(lifecycleOwner.lifecycle).willReturn(lifecycle)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        viewModel = RegisterViewModel(stringProvider, firebaseProvider)
    }

    @Test
    fun `it should initialize fields properly`() {
        assertFalse(viewModel.isHideKeyBoard.value?.peekContent()!!)
        assertNull(viewModel.nameErrorMessage.value)
        assertNull(viewModel.emailErrorMessage.value)
        assertNull(viewModel.passwordErrorMessage.value)
        assertNull(viewModel.errorSaveUser.value)
        assertNull(viewModel.createUser.value)
        assertFalse(viewModel.isUserSaved.value?.peekContent()!!)
    }

    @Test
    fun `should return name error message when validate data entry`() {

        viewModel.isHideKeyBoard.observe(lifecycleOwner, observeIsHideKeyBoard)
        viewModel.nameErrorMessage.observe(lifecycleOwner, observeNameErrorMessage)
        viewModel.emailErrorMessage.observe(lifecycleOwner, observeEmailErrorMessage)
        viewModel.passwordErrorMessage.observe(lifecycleOwner, observePasswordErrorMessage)

        val userWithoutName = User()
        userWithoutName.name = ""
        userWithoutName.email = ""
        userWithoutName.password = ""

        viewModel.user = userWithoutName

        viewModel.validateDataEntry()

        verify(observeIsHideKeyBoard, times(2)).onChanged(any())
        verify(observeNameErrorMessage, times(1)).onChanged(any())
        verify(observeEmailErrorMessage, times(0)).onChanged(any())
        verify(observePasswordErrorMessage, times(0)).onChanged(any())

    }

    @Test
    fun `should return email error message when validate data entry`() {

        viewModel.isHideKeyBoard.observe(lifecycleOwner, observeIsHideKeyBoard)
        viewModel.nameErrorMessage.observe(lifecycleOwner, observeNameErrorMessage)
        viewModel.emailErrorMessage.observe(lifecycleOwner, observeEmailErrorMessage)
        viewModel.passwordErrorMessage.observe(lifecycleOwner, observePasswordErrorMessage)

        val userWithoutName = User()
        userWithoutName.name = "Nome"
        userWithoutName.email = ""
        userWithoutName.password = ""

        viewModel.user = userWithoutName

        viewModel.validateDataEntry()

        verify(observeIsHideKeyBoard, times(2)).onChanged(any())
        verify(observeNameErrorMessage, times(0)).onChanged(any())
        verify(observeEmailErrorMessage, times(1)).onChanged(any())
        verify(observePasswordErrorMessage, times(0)).onChanged(any())

    }

    @Test
    fun `should return password error message when validate data entry`() {

        viewModel.isHideKeyBoard.observe(lifecycleOwner, observeIsHideKeyBoard)
        viewModel.nameErrorMessage.observe(lifecycleOwner, observeNameErrorMessage)
        viewModel.emailErrorMessage.observe(lifecycleOwner, observeEmailErrorMessage)
        viewModel.passwordErrorMessage.observe(lifecycleOwner, observePasswordErrorMessage)

        val userWithoutName = User()
        userWithoutName.name = "Nome"
        userWithoutName.email = "teste@teste.com"
        userWithoutName.password = ""

        viewModel.user = userWithoutName

        viewModel.validateDataEntry()

        verify(observeIsHideKeyBoard, times(2)).onChanged(any())
        verify(observeNameErrorMessage, times(0)).onChanged(any())
        verify(observeEmailErrorMessage, times(0)).onChanged(any())
        verify(observePasswordErrorMessage, times(1)).onChanged(any())

    }

    @Test
    fun `should call create user when call create user`() {

        viewModel.isHideKeyBoard.observe(lifecycleOwner, observeIsHideKeyBoard)
        viewModel.nameErrorMessage.observe(lifecycleOwner, observeNameErrorMessage)
        viewModel.emailErrorMessage.observe(lifecycleOwner, observeEmailErrorMessage)
        viewModel.passwordErrorMessage.observe(lifecycleOwner, observePasswordErrorMessage)
        viewModel.createUser.observe(lifecycleOwner, observeCreateUser)

        val userWithoutName = User()
        userWithoutName.name = "Nome"
        userWithoutName.email = "teste@teste.com"
        userWithoutName.password = "123456"

        viewModel.user = userWithoutName

        viewModel.validateDataEntry()

        verify(observeIsHideKeyBoard, times(2)).onChanged(any())
        verify(observeNameErrorMessage, times(0)).onChanged(any())
        verify(observeEmailErrorMessage, times(0)).onChanged(any())
        verify(observePasswordErrorMessage, times(0)).onChanged(any())
        verify(observeCreateUser, times(1)).onChanged(any())

    }
}
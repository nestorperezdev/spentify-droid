package com.nestor.auth.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nestor.auth.R
import com.nestor.uikit.button.SYButton
import com.nestor.uikit.input.FormFieldData
import com.nestor.uikit.input.SYInputField
import com.nestor.uikit.input.action.Action
import com.nestor.uikit.input.action.InputFieldAction
import com.nestor.uikit.loading.LoadingScreen
import com.nestor.uikit.snackbar.SYSnackbar
import com.nestor.uikit.statusbar.NavigationIcon
import com.nestor.uikit.statusbar.SYStatusBar
import com.nestor.uikit.statusbar.StatusBarType
import com.nestor.uikit.theme.spacing.LocalSYPadding
import com.nestor.uikit.util.stringResourceNullable

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    initialEmailValue: String? = null,
    onForgotPasswordClick: () -> Unit = {},
    onNavigationBackClick: () -> Unit = {},
    onSignupClick: () -> Unit = {},
    onSuccessLogin: () -> Unit = {},
) {
    val uiState = loginViewModel.uiState.collectAsState().value
    LaunchedEffect(initialEmailValue) {
        initialEmailValue?.let {
            loginViewModel.onEmailChanged(it)
        }
    }
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onSuccessLogin()
        }
    }
    if (uiState.isLoading) {
        LoadingScreen(text = stringResource(R.string.login))
    } else {
        LoginScreenContent(
            onNavigationBackClick = onNavigationBackClick,
            email = uiState.email,
            onEmailChange = loginViewModel::onEmailChanged,
            password = uiState.password,
            onPasswordChanged = loginViewModel::onPasswordChange,
            errorResource = uiState.loginErrorResource,
            onShowFormInvalidToastDismissed = loginViewModel::onErrorToastDismissed,
            onSubmit = loginViewModel::onSubmit,
            onSignupClick = onSignupClick,
            onForgotPasswordClick = onForgotPasswordClick
        )
    }
}

@Composable
internal fun LoginScreenContent(
    email: FormFieldData = FormFieldData(""),
    onEmailChange: (String) -> Unit = {},
    password: FormFieldData = FormFieldData(""),
    onPasswordChanged: (String) -> Unit = {},
    onNavigationBackClick: () -> Unit = {},
    errorResource: Int? = null,
    onForgotPasswordClick: () -> Unit = {},
    onShowFormInvalidToastDismissed: () -> Unit = {},
    onSignupClick: () -> Unit = {},
    onSubmit: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                SYSnackbar(it)
            }
        },
        topBar = {
            SYStatusBar(
                barType = StatusBarType.OnlyNavigation(navigationIcon = NavigationIcon.Close {
                    onNavigationBackClick()
                })
            )
        }) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = LocalSYPadding.current.screenHorizontalPadding)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(70.dp))
            Text(
                text = stringResource(R.string.sign_into_your_account),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = stringResource(R.string.log_into_your_spentify_account),
                modifier = Modifier
                    .fillMaxWidth(0.80f)
                    .padding(top = 16.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 45.dp),
                verticalArrangement = Arrangement.spacedBy(21.dp)
            ) {
                val emailFocusRequester = remember { FocusRequester() }
                val passwordFocusRequester = remember { FocusRequester() }
                SYInputField(
                    modifier = Modifier.focusRequester(emailFocusRequester),
                    value = email.value,
                    onValueChange = onEmailChange,
                    label = "Email",
                    placeholder = "johndoe@email.com",
                    keyboardActions = KeyboardActions(onNext = { passwordFocusRequester.requestFocus() }),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Email
                    ),
                    isError = email.hasError,
                    error = stringResourceNullable(email.errorResource)
                )
                var passwordVisibility by remember { mutableStateOf(false) }
                SYInputField(
                    modifier = Modifier.focusRequester(passwordFocusRequester),
                    value = password.value,
                    onValueChange = onPasswordChanged,
                    label = "Password",
                    placeholder = "********",
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardActions = KeyboardActions(onDone = { onSubmit() }),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password
                    ),
                    isError = password.hasError,
                    error = stringResourceNullable(password.errorResource),
                    action = InputFieldAction.TrailingAction(Action(ImageVector.vectorResource(R.drawable.baseline_visibility_24)) {
                        passwordVisibility = !passwordVisibility
                    })
                )
                Column {
                    Text(text = stringResource(R.string.have_you_forgotten_your_password))
                    Text(
                        text = stringResource(R.string.click_here_to_recover_it),
                        modifier = Modifier.clickable { onForgotPasswordClick() },
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.heightIn(min = 35.dp))
            Spacer(modifier = Modifier.weight(1f))
            SYButton(
                onClick = onSubmit,
                buttonText = stringResource(R.string.log_in),
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.do_you_not_have_a_spentify_account))
                    withStyle(
                        LocalTextStyle.current
                            .copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.W600
                            )
                            .toSpanStyle()
                    ) {
                        append(stringResource(R.string.sign_up_here))
                    }
                },
                modifier = Modifier
                    .padding(top = 18.dp)
                    .clickable { onSignupClick() }
            )
            Spacer(modifier = Modifier.height(LocalSYPadding.current.screenBottomPadding))
        }
    }
    val message = stringResourceNullable(errorResource)
    LaunchedEffect(errorResource) {
        errorResource?.let {
            message?.let {
                snackbarHostState.showSnackbar(it)
                onShowFormInvalidToastDismissed()
            }
        }
    }
}

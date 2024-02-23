package com.nestor.auth.ui.signup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization.Companion.Words
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withAnnotation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nestor.auth.R
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.button.SYButton
import com.nestor.uikit.input.FormFieldData
import com.nestor.uikit.input.SYInputField
import com.nestor.uikit.input.action.Action
import com.nestor.uikit.input.action.InputFieldAction
import com.nestor.uikit.snackbar.SYSnackbar
import com.nestor.uikit.statusbar.NavigationIcon
import com.nestor.uikit.statusbar.SYStatusBar
import com.nestor.uikit.statusbar.StatusBarType
import com.nestor.uikit.theme.spacing.LocalSYPadding
import com.nestor.uikit.util.stringResourceNullable

@Composable
fun SignupScreen(
    onNavigationBackClick: () -> Unit,
    onLoginClick: (String?) -> Unit,
    onRecoverPassword: (String?) -> Unit,
    viewModel: SignupViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value
    if (uiState.isLoading) {
        Text(text = "Loading...")
    } else {
        SignupScreenContent(
            onNavigationBackClick = onNavigationBackClick,
            onLoginClick = onLoginClick,
            onRecoverPasswordClick = onRecoverPassword,
            name = uiState.name,
            onNameChange = viewModel::onNameChanged,
            email = uiState.email,
            onEmailChange = viewModel::onEmailChanged,
            password = uiState.password,
            onPasswordChanged = viewModel::onPasswordChange,
            repeatPassword = uiState.repeatPassword,
            onRepeatPasswordChanged = viewModel::onPasswordRepeatChange,
            alreadyRegistered = uiState.isRegistered,
            showFormInvalidToast = uiState.showFormInvalidToast,
            onShowFormInvalidToastDismissed = viewModel::onShowFormInvalidToastDismissed,
            onSubmit = viewModel::onSubmit
        )
    }
}

@Composable
private fun SignupScreenContent(
    onNavigationBackClick: () -> Unit,
    onLoginClick: (String?) -> Unit,
    onRecoverPasswordClick: (String?) -> Unit,
    alreadyRegistered: Boolean,
    name: FormFieldData,
    onNameChange: (String) -> Unit,
    email: FormFieldData,
    onEmailChange: (String) -> Unit,
    password: FormFieldData,
    onPasswordChanged: (String) -> Unit,
    repeatPassword: FormFieldData,
    onRepeatPasswordChanged: (String) -> Unit,
    showFormInvalidToast: Boolean,
    onShowFormInvalidToastDismissed: () -> Unit,
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
                text = stringResource(R.string.create_account),
                style = MaterialTheme.typography.titleLarge
            )
            /**
             * Should take a max of 75% of the screen
             */
            Column(
                modifier = Modifier.fillMaxWidth(0.80f),
            ) {
                Text(text = stringResource(R.string.open_a_spentify_account_with_few_details))
                if (alreadyRegistered) {
                    AlreadyRegisteredUserView(
                        modifier = Modifier.padding(top = 13.dp),
                        onLoginClick = { onLoginClick(email.value) },
                        onRecoverPasswordClick = { onRecoverPasswordClick(email.value) }
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 45.dp),
                verticalArrangement = spacedBy(21.dp)
            ) {
                val emailFocusRequester = remember { FocusRequester() }
                val passwordFocusRequester = remember { FocusRequester() }
                val repeatPasswordFocusRequester = remember { FocusRequester() }
                SYInputField(
                    value = name.value,
                    onValueChange = onNameChange,
                    label = "Full name",
                    placeholder = "John Doe",
                    keyboardActions = KeyboardActions(onNext = { emailFocusRequester.requestFocus() }),
                    keyboardOptions = KeyboardOptions(
                        capitalization = Words,
                        imeAction = ImeAction.Next
                    ),
                    isError = name.hasError,
                    error = stringResourceNullable(name.errorResource)
                )
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
                    keyboardActions = KeyboardActions(onNext = { repeatPasswordFocusRequester.requestFocus() }),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Password
                    ),
                    isError = password.hasError,
                    error = stringResourceNullable(password.errorResource),
                    action = InputFieldAction.TrailingAction(Action(ImageVector.vectorResource(R.drawable.baseline_visibility_24)) {
                        passwordVisibility = !passwordVisibility
                    })
                )
                var repeatPasswordVisibility by remember { mutableStateOf(false) }
                SYInputField(
                    modifier = Modifier.focusRequester(repeatPasswordFocusRequester),
                    value = repeatPassword.value,
                    onValueChange = onRepeatPasswordChanged,
                    label = "Repeat password",
                    placeholder = "********",
                    visualTransformation = if (repeatPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardActions = KeyboardActions(onDone = { onSubmit() }),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password
                    ),
                    isError = repeatPassword.hasError,
                    error = stringResourceNullable(repeatPassword.errorResource),
                    action = InputFieldAction.TrailingAction(Action(ImageVector.vectorResource(R.drawable.baseline_visibility_24)) {
                        repeatPasswordVisibility = !repeatPasswordVisibility
                    })
                )
            }
            Spacer(modifier = Modifier.heightIn(min = 35.dp))
            Spacer(modifier = Modifier.weight(1f))
            SYButton(
                onClick = onSubmit,
                buttonText = stringResource(R.string.create_your_account),
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.do_you_already_have_a_spentify_account))
                    withStyle(
                        LocalTextStyle.current
                            .copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.W600
                            )
                            .toSpanStyle()
                    ) {
                        append(stringResource(R.string.sign_in_here))
                    }
                },
                modifier = Modifier
                    .padding(top = 18.dp)
                    .clickable { onLoginClick(null) }
            )
            Spacer(modifier = Modifier.height(LocalSYPadding.current.screenBottomPadding))
        }
    }
    val message = stringResource(R.string.form_is_invalid_please_check_the_fields_and_try_again)
    LaunchedEffect(showFormInvalidToast) {
        if (showFormInvalidToast) {
            snackbarHostState.showSnackbar(message)
            onShowFormInvalidToastDismissed()
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun AlreadyRegisteredUserView(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,
    onRecoverPasswordClick: () -> Unit,
) {
    val hyperlink = LocalTextStyle.current
        .copy(
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.W600
        )
        .toSpanStyle()
    val signInText = " " + stringResource(R.string.go_to_sign)
    val recoverPasswordText = " " + stringResource(R.string.recover_password)
    val text = buildAnnotatedString {
        withStyle(
            LocalTextStyle.current
                .copy(color = MaterialTheme.colorScheme.error)
                .toSpanStyle()
        ) {
            append(stringResource(R.string.oops_looks_like_there_s_a_user_registered))
            withAnnotation("signin", "signin") {
                withStyle(hyperlink) {
                    append(signInText)
                }
            }
            append(" " + stringResource(R.string.or))
            withAnnotation("recover", "recover") {
                withStyle(hyperlink) {
                    append(recoverPasswordText)
                }
            }
        }
    }
    ClickableText(text, modifier = modifier) { offset ->
        text.getStringAnnotations(offset, offset).firstOrNull()?.tag.let { tag ->
            if (tag == "signin") {
                onLoginClick()
            } else if (tag == "recover") {
                onRecoverPasswordClick()
            }
        }
    }
}

@Preview
@Composable
fun SignupScreenContentPreview() {
    SpentifyTheme {
        var name by remember { mutableStateOf(FormFieldData("")) }
        var email by remember { mutableStateOf(FormFieldData("")) }
        var password by remember { mutableStateOf(FormFieldData("")) }
        var repeatPassword by remember { mutableStateOf(FormFieldData("")) }
        SignupScreenContent(
            onLoginClick = {},
            onNavigationBackClick = {},
            name = name,
            onNameChange = { name = name.copy(value = it) },
            email = email,
            onEmailChange = { email = email.copy(value = it) },
            password = password,
            onPasswordChanged = { password = password.copy(value = it) },
            repeatPassword = repeatPassword,
            onRecoverPasswordClick = {},
            alreadyRegistered = true,
            showFormInvalidToast = false,
            onShowFormInvalidToastDismissed = {},
            onRepeatPasswordChanged = { repeatPassword = repeatPassword.copy(value = it) }
        )
    }
}
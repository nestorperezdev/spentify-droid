package com.nestor.auth.ui.recoverpassword

import androidx.compose.foundation.layout.Arrangement.spacedBy
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
import com.nestor.uikit.loading.LoadingScreen
import com.nestor.uikit.snackbar.SYSnackbar
import com.nestor.uikit.statusbar.NavigationIcon
import com.nestor.uikit.statusbar.SYStatusBar
import com.nestor.uikit.statusbar.StatusBarType
import com.nestor.uikit.theme.spacing.LocalSYPadding
import com.nestor.uikit.util.stringResourceNullable

@Composable
fun RecoverPasswordScreen(
    onNavigationBackClick: () -> Unit,
    onGetStartedClick: () -> Unit = {},
    viewModel: RecoverPasswordViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value
    if (uiState.isLoading) {
        LoadingScreen()
    } else if (uiState.isDone) {
        RecoverPasswordSuccessScreen(onGetStartedClick = onGetStartedClick)
    } else {
        RecoverPasswordScreenContent(
            onNavigationBackClick = onNavigationBackClick,
            password = uiState.password,
            onPasswordChanged = viewModel::onPasswordChange,
            repeatPassword = uiState.repeatPassword,
            onRepeatPasswordChanged = viewModel::onPasswordRepeatChange,
            showFormInvalidToast = uiState.showFormInvalidToast,
            onShowFormInvalidToastDismissed = viewModel::onShowFormInvalidToastDismissed,
            onSubmit = viewModel::onSubmit
        )
    }
}

@Composable
private fun RecoverPasswordScreenContent(
    onNavigationBackClick: () -> Unit,
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
                text = stringResource(R.string.recover_password_2),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = stringResource(R.string.please_enter_your_new_password_to_continue),
                modifier = Modifier.fillMaxWidth(0.80f)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 45.dp),
                verticalArrangement = spacedBy(21.dp)
            ) {
                val repeatPasswordFocusRequester = remember { FocusRequester() }
                var passwordVisibility by remember { mutableStateOf(false) }
                SYInputField(
                    value = password.value,
                    onValueChange = onPasswordChanged,
                    label = stringResource(R.string.new_password),
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
                    label = stringResource(R.string.repeat_password),
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
                buttonText = stringResource(R.string.change_password),
                modifier = Modifier.fillMaxWidth()
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

@Composable
private fun RecoverPasswordSuccessScreen(
    onGetStartedClick: () -> Unit
) {
    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = LocalSYPadding.current.screenHorizontalPadding)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(70.dp))
            Text(
                text = stringResource(R.string.password_changed),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = stringResource(R.string.your_new_password_has_been_stored),
                modifier = Modifier
                    .fillMaxWidth(0.80f)
                    .padding(top = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            SYButton(
                onClick = { onGetStartedClick() },
                buttonText = stringResource(R.string.start_using_spentify),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(LocalSYPadding.current.screenBottomPadding))
        }
    }
}

@Preview
@Composable
fun RecoverPasswordScreenContentPreview() {
    SpentifyTheme {
        var password by remember { mutableStateOf(FormFieldData("")) }
        var repeatPassword by remember { mutableStateOf(FormFieldData("")) }
        RecoverPasswordScreenContent(
            onNavigationBackClick = {},
            password = password,
            onPasswordChanged = { password = password.copy(value = it) },
            repeatPassword = repeatPassword,
            showFormInvalidToast = false,
            onShowFormInvalidToastDismissed = {},
            onRepeatPasswordChanged = { repeatPassword = repeatPassword.copy(value = it) }
        )
    }
}

@Preview
@Composable
fun RecoverPasswordContentPreview() {
    SpentifyTheme {
        RecoverPasswordSuccessScreen(onGetStartedClick = {})
    }
}
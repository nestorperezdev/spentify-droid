package com.nestor.auth.ui.forgotpassword

import android.content.Intent
import android.content.Intent.ACTION_MAIN
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nestor.auth.R
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.button.SYButton
import com.nestor.uikit.input.FormFieldData
import com.nestor.uikit.input.SYInputField
import com.nestor.uikit.loading.LoadingScreen
import com.nestor.uikit.snackbar.SYSnackbar
import com.nestor.uikit.statusbar.NavigationIcon
import com.nestor.uikit.statusbar.SYStatusBar
import com.nestor.uikit.statusbar.StatusBarType
import com.nestor.uikit.theme.spacing.LocalSYPadding
import com.nestor.uikit.util.stringResourceNullable

@Composable
fun ForgotPasswordScreen(
    forgotPasswordVM: ForgotPasswordViewModel = hiltViewModel(),
    onNavigationBackClick: () -> Unit = {},
) {
    val uiState = forgotPasswordVM.uiState.collectAsState().value
    if (uiState.isLoading) {
        LoadingScreen()
    } else if (uiState.successResponse) {
        ForgotPasswordEmailSent()
    } else {
        ForgotPasswordScreenContent(
            email = uiState.email,
            onEmailChange = forgotPasswordVM::onEmailChanged,
            onNavigationBackClick = onNavigationBackClick,
            errorResource = uiState.loginErrorResource,
            onShowFormInvalidToastDismissed = forgotPasswordVM::onErrorToastDismissed,
            onSubmit = forgotPasswordVM::onSubmit
        )
    }
}

@Composable
private fun ForgotPasswordScreenContent(
    email: FormFieldData = FormFieldData(""),
    onEmailChange: (String) -> Unit = {},
    onNavigationBackClick: () -> Unit = {},
    errorResource: Int? = null,
    onShowFormInvalidToastDismissed: () -> Unit = {},
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
                text = stringResource(R.string.forgot_password),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Please enter your email to recover your password.",
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
                SYInputField(
                    value = email.value,
                    onValueChange = onEmailChange,
                    label = "Email",
                    placeholder = "johndoe@email.com",
                    keyboardActions = KeyboardActions(onDone = { onSubmit.invoke() }),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Email
                    ),
                    isError = email.hasError,
                    error = stringResourceNullable(email.errorResource)
                )
            }
            Spacer(modifier = Modifier.heightIn(min = 35.dp))
            Spacer(modifier = Modifier.weight(1f))
            SYButton(
                onClick = onSubmit,
                buttonText = stringResource(R.string.recover_password_action),
                modifier = Modifier.fillMaxWidth()
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

@Composable
private fun ForgotPasswordEmailSent() {
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
                text = stringResource(R.string.done),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = stringResource(R.string.we_ve_sent_recover_password_instructions),
                modifier = Modifier
                    .fillMaxWidth(0.80f)
                    .padding(top = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            val context = LocalContext.current
            SYButton(
                onClick = {
                    val intent = Intent(ACTION_MAIN)
                    intent.addCategory(Intent.CATEGORY_APP_EMAIL)
                    context.startActivity(intent)
                },
                buttonText = stringResource(R.string.open_my_email),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(LocalSYPadding.current.screenBottomPadding))
        }
    }
}

@Preview
@Composable
private fun ForgotPasswordPreview() {
    SpentifyTheme {
        ForgotPasswordScreenContent()
    }
}

@Preview
@Composable
private fun ForgotPasswordEmailSentPreview() {
    SpentifyTheme {
        ForgotPasswordEmailSent()
    }
}
package com.nestor.auth.signup

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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization.Companion.Words
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nestor.login.R
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.button.SYButton
import com.nestor.uikit.input.SYInputField
import com.nestor.uikit.statusbar.NavigationIcon
import com.nestor.uikit.statusbar.SYStatusBar
import com.nestor.uikit.statusbar.StatusBarType
import com.nestor.uikit.theme.spacing.LocalSYPadding

@Composable
fun SignupScreen(
    onNavigationBackClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    SignupScreenContent(
        onNavigationBackClick = onNavigationBackClick,
        onLoginClick = onLoginClick
    )
}

@Composable
private fun SignupScreenContent(
    onNavigationBackClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Scaffold(topBar = {
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
            Text(text = stringResource(R.string.open_a_spentify_account_with_few_details))
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
                    value = "",
                    onValueChange = {},
                    label = "Full name",
                    placeholder = "John Doe",
                    keyboardActions = KeyboardActions(onNext = { emailFocusRequester.requestFocus() }),
                    keyboardOptions = KeyboardOptions(
                        capitalization = Words,
                        imeAction = ImeAction.Next
                    )
                )
                SYInputField(
                    modifier = Modifier.focusRequester(emailFocusRequester),
                    value = "",
                    onValueChange = {},
                    label = "Email",
                    placeholder = "johndoe@email.com",
                    keyboardActions = KeyboardActions(onNext = { passwordFocusRequester.requestFocus() }),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Email
                    )
                )
                SYInputField(
                    modifier = Modifier.focusRequester(passwordFocusRequester),
                    value = "",
                    onValueChange = {},
                    label = "Password",
                    placeholder = "********",
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardActions = KeyboardActions(onNext = { repeatPasswordFocusRequester.requestFocus() }),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
                SYInputField(
                    modifier = Modifier.focusRequester(repeatPasswordFocusRequester),
                    value = "",
                    onValueChange = {},
                    label = "Repeat password",
                    placeholder = "********",
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardActions = KeyboardActions(onDone = { /*TODO*/ }),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
            }
            Spacer(modifier = Modifier.heightIn(min = 35.dp))
            Spacer(modifier = Modifier.weight(1f))
            SYButton(
                onClick = { /*TODO*/ },
                buttonText = stringResource(R.string.create_your_account),
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Do you already have a BankMe account? Sign in here",
                modifier = Modifier
                    .padding(top = 18.dp)
                    .clickable { onLoginClick() }
            )
            Spacer(modifier = Modifier.height(LocalSYPadding.current.screenBottomPadding))
        }
    }
}

@Preview
@Composable
fun SignupScreenContentPreview() {
    SpentifyTheme {
        SignupScreenContent(onLoginClick = {}, onNavigationBackClick = {})
    }
}
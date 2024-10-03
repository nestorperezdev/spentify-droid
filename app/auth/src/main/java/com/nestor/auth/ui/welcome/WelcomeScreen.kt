package com.nestor.auth.ui.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nestor.auth.R
import com.nestor.uikit.button.SYButton
import com.nestor.uikit.button.SYOutlinedButton
import com.nestor.uikit.theme.spacing.LocalSYPadding

@Composable
fun WelcomeScreen(
    onLoginClick: () -> Unit,
    onSignupClick: () -> Unit
) {
    WelcomeScreenContent(onLoginClick, onSignupClick)
}

@Composable
internal fun WelcomeScreenContent(
    onLoginClick: () -> Unit,
    onSignupClick: () -> Unit
) {
    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(bottom = LocalSYPadding.current.screenBottomPadding)
                .padding(horizontal = LocalSYPadding.current.screenHorizontalPadding)
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                stringResource(R.string.welcome_to_spentify),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Text(
                text = stringResource(R.string.a_expense_tracking_platform),
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(160.dp))
            SYButton(
                onClick = onSignupClick,
                buttonText = stringResource(R.string.create_your_free_account),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            )
            SYOutlinedButton(
                onClick = onLoginClick,
                buttonText = stringResource(R.string.login_into_your_account),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

package com.nestor.account.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nestor.account.R
import com.nestor.uikit.SpentifyTheme

@Composable
fun PrivacyDialog(onDismissRequest: () -> Unit = {}) {
    DialogBase(
        onDismissRequest = onDismissRequest,
        statusBarTitle = "Privacy Policy"
    ) {
        Column(
            modifier = it.verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = "Privacy Policy",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(id = R.string.privacy_policy),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Preview
@Composable
private fun PrivacyDialogPreview() {
    SpentifyTheme {
        PrivacyDialog()
    }
}
package com.nestor.uikit.keyboard

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nestor.uikit.R
import com.nestor.uikit.SpentifyTheme

private data class SYKeyboardKey(val symbol: SYKeyboardSymbol, val id: Key)
private sealed class SYKeyboardSymbol(
    open val vectorResource: Int? = null,
    open val symbol: String? = null
) {
    data class SYKeyboardStringSymbol(override val symbol: String) : SYKeyboardSymbol()
    data class SYKeyboardVectorSymbol(override val vectorResource: Int?) : SYKeyboardSymbol()
}

private val keyboardSymbols = listOf(
    listOf(
        SYKeyboardKey(SYKeyboardSymbol.SYKeyboardStringSymbol("1"), Key.NumPad1),
        SYKeyboardKey(SYKeyboardSymbol.SYKeyboardStringSymbol("2"), Key.NumPad2),
        SYKeyboardKey(SYKeyboardSymbol.SYKeyboardStringSymbol("3"), Key.NumPad3)
    ),
    listOf(
        SYKeyboardKey(SYKeyboardSymbol.SYKeyboardStringSymbol("4"), Key.NumPad4),
        SYKeyboardKey(SYKeyboardSymbol.SYKeyboardStringSymbol("5"), Key.NumPad5),
        SYKeyboardKey(SYKeyboardSymbol.SYKeyboardStringSymbol("6"), Key.NumPad6)
    ),
    listOf(
        SYKeyboardKey(SYKeyboardSymbol.SYKeyboardStringSymbol("7"), Key.NumPad7),
        SYKeyboardKey(SYKeyboardSymbol.SYKeyboardStringSymbol("8"), Key.NumPad8),
        SYKeyboardKey(SYKeyboardSymbol.SYKeyboardStringSymbol("9"), Key.NumPad9)
    ),
    listOf(
        SYKeyboardKey(SYKeyboardSymbol.SYKeyboardStringSymbol("."), Key.NumPadDot),
        SYKeyboardKey(SYKeyboardSymbol.SYKeyboardStringSymbol("0"), Key.NumPad0),
        SYKeyboardKey(
            SYKeyboardSymbol.SYKeyboardVectorSymbol(R.drawable.baseline_backspace_24),
            Key.Backspace
        )
    ),
)


@Composable
fun SYKeyboard(modifier: Modifier = Modifier, onKeyPress: (Key) -> Unit) {
    val rippleEffect = rememberRipple(bounded = false)
    CompositionLocalProvider(LocalIndication provides rippleEffect) {
        Column(modifier = modifier.aspectRatio(3 / 4f)) {
            keyboardSymbols.forEach { keyRow ->
                Row(modifier = Modifier.weight(1 / 4f)) {
                    keyRow.forEach { key ->
                        SYKey(
                            modifier = Modifier
                                .clickable { onKeyPress(key.id) }
                                .weight(1 / 3f)
                                .fillMaxSize(),
                            symbol = key.symbol
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SYKey(modifier: Modifier, symbol: SYKeyboardSymbol) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (symbol) {
            is SYKeyboardSymbol.SYKeyboardVectorSymbol -> Icon(
                painterResource(id = symbol.vectorResource!!),
                null,
                modifier = Modifier.size(40.dp)
            )

            is SYKeyboardSymbol.SYKeyboardStringSymbol -> Text(
                text = symbol.symbol,
                fontSize = 40.sp
            )
        }
    }
}

@Preview
@Composable
private fun SYKeyboardPreview() {
    var keyPressed by remember {
        mutableStateOf<Key?>(null)
    }
    SpentifyTheme {
        Scaffold {
            Column(modifier = Modifier.padding(it)) {
                Text(text = "Keypressed:")
                keyPressed?.let {
                    Text(text = "${it.keyCode}")
                }
                Spacer(modifier = Modifier.weight(1f))
                SYKeyboard(modifier = Modifier.fillMaxWidth()) {
                    keyPressed = it
                }
            }
        }
    }
}
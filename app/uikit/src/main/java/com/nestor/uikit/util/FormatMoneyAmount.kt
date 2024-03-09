package com.nestor.uikit.util

/**
 * Given a double amount it will format:
 * ex:
 * 23.454545 -> 23.45
 * 23.00 -> 23
 */
fun Double.formatMoneyAmount(): String {
    return if (this % 1 == 0.0) {
        String.format("%.0f", this)
    } else {
        String.format("%.2f", this)
    }
}
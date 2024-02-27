package com.nestor.uikit.util

import androidx.navigation.NavController

fun NavController.isRouteOnPreviousStack(route: String): Boolean {
    return try {
        this.getBackStackEntry(route)
        true
    } catch (e: IllegalArgumentException) {
        false
    }
}
package com.nestor.common.data.statusbar

import com.nestor.uikit.statusbar.StatusBarType
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface StatusBarRepository {
    fun updateStatusBar(statusBar: StatusBarType)
    fun statusBarType(): StateFlow<StatusBarType>
}
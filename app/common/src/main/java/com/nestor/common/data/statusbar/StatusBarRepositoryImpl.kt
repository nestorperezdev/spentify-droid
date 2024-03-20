package com.nestor.common.data.statusbar

import com.nestor.uikit.statusbar.StatusBarType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


class StatusBarRepositoryImpl @Inject constructor() : StatusBarRepository {
    private val statusBarTypeFlow: MutableStateFlow<StatusBarType> =
        MutableStateFlow(StatusBarType.LoadingDoubleLineStatusBar)

    override fun updateStatusBar(statusBar: StatusBarType) {
        statusBarTypeFlow.update { statusBar }
    }

    override fun statusBarType(): StateFlow<StatusBarType> = statusBarTypeFlow
}
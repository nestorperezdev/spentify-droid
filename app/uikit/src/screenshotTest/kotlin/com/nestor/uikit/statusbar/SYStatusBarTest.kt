package com.nestor.uikit.statusbar

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class SYStatusBarTest {
    @Preview
    @Composable
    fun SYStatusBarPreviewLoadingTest() {
        SYStatusBarPreviewLoading()
    }

    @Preview
    @Composable
    fun SYStatusBarPreviewNavigationTest() {
        SYStatusBarPreviewNavigation()
    }

    @Preview
    @Composable
    fun SYStatusBarPreviewActionTest() {
        SYStatusBarPreviewAction()
    }


    @Preview
    @Composable
    fun SYStatusBarPreviewLeftAndNavigationTest() {
        SYStatusBarPreviewLeftAndNavigation()
    }


    @Preview
    @Composable
    fun SYStatusBarPreviewCenterTitleTest() {
        SYStatusBarPreviewCenterTitle()
    }


    @Preview
    @Composable
    fun SYStatusBarPreviewTitleNSubtitleTest() {
        SYStatusBarPreviewTitleNSubtitle()
    }


    @Preview
    @Composable
    fun SYStatusBarPreviewTitleTest() {
        SYStatusBarPreviewTitle()
    }

}
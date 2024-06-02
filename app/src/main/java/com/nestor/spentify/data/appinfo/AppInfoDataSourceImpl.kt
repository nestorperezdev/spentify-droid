package com.nestor.spentify.data.appinfo

import com.nestor.spentify.BuildConfig
import com.nestor.common.data.appinfo.AppInfoDataSource
import javax.inject.Inject

class AppInfoDataSourceImpl @Inject constructor() : AppInfoDataSource {
    override fun appVersion(): String {
        return BuildConfig.VERSION_NAME
    }

    override fun appConfig(): String {
        return BuildConfig.BUILD_TYPE
    }
}
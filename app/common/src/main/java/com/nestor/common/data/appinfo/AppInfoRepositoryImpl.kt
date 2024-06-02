package com.nestor.common.data.appinfo

import javax.inject.Inject

class AppInfoRepositoryImpl @Inject constructor(
    private val appInfoDataSource: AppInfoDataSource
) : AppInfoRepository {
    override fun appVersion(): String {
        return appInfoDataSource.appVersion()
    }

    override fun appConfig(): String {
        return appInfoDataSource.appConfig()
    }
}
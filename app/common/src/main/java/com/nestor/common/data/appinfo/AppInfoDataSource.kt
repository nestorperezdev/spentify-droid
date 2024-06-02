package com.nestor.common.data.appinfo

interface AppInfoDataSource {
    fun appVersion(): String
    fun appConfig(): String
}
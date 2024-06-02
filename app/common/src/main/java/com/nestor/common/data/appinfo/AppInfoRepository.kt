package com.nestor.common.data.appinfo

interface AppInfoRepository {
    /**
     * Semantic format: 1.0.0.0 (last char is build number)
     */
    fun appVersion(): String

    /**
     * debug, release, etc.
     */
    fun appConfig(): String
}
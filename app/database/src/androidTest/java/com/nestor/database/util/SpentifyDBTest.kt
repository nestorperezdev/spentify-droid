package com.nestor.database.util

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nestor.database.SpentifyDatabase
import com.nestor.database.SpentifyDatabaseUtilTest
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
abstract class SpentifyDBTest {
    lateinit var db: SpentifyDatabase
    @Before
    fun createDb() {
        db = SpentifyDatabaseUtilTest.createDb(ApplicationProvider.getApplicationContext())
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
}
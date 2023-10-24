package com.example.beenthere.data.source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.content.pm.ApplicationInfoBuilder
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.beenthere.data.Experience
import com.example.beenthere.getOrAwaitValue

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlinx.coroutines.test.runBlockingTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class BeenThereDatabaseDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: BeenThereDatabase
    private lateinit var dao: BeenThereDatabaseDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            BeenThereDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.beenThereDatabaseDao
    }

    @After
    fun tearDown() {
        database.close()
    }


    @Test
    fun insertExp() = runBlockingTest {
        val exp = Experience(
            userId = "A",
            title = "Sapiens",
            author = "Yuval",
            situation = "My friend keeps complaining about his life",
            phrases = "Human beings defeated those animals that were 100 times bigger than them",
            image = "",
            isProcessed = false,
            category = "Life Meaning"
        )
        dao.insert(exp)


        val allExp = dao.observeALlExp().getOrAwaitValue()

        assertThat(allExp).contains(exp)
    }

    @Test
    fun deleteExp() = runBlockingTest {
        val exp = Experience(
            userId = "A",
            title = "Sapiens",
            author = "Yuval",
            situation = "My friend keeps complaining about his life",
            phrases = "Human beings defeated those animals that were 100 times bigger than them",
            image = "",
            isProcessed = false,
            category = "Life Meaning"
        )
        dao.insert(exp)
        dao.delete(exp)


        val allExp = dao.observeALlExp().getOrAwaitValue()

        assertThat(allExp).doesNotContain(exp)
    }


}
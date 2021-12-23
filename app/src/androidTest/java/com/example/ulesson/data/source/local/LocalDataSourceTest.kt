package com.example.ulesson.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.ulesson.data.helper.Resource
import com.example.ulesson.util.TestObjectUtil
import com.example.ulesson.util.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class LocalDataSourceTest {
    private lateinit var localDataSource: SubjectLocalDataSourceImpl
    private lateinit var database: ULessonDatabase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ULessonDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        localDataSource =
            SubjectLocalDataSourceImpl(
                database.subjectDao(),
                database.recentViewDao(),
                Dispatchers.Main
            )

        runBlocking {
            saveSubjects()
            saveReceiverViews()
        }
    }

    @Test
    fun saveSubjectsAndRetrieveSingleSubject() = runBlocking {
        val subject = TestObjectUtil.subjects[0]
        val response = localDataSource.getSubject(subject.id)
        assertThat(response, `is`(Resource.Success(subject)))
    }

    @Test
    fun saveSubjectsAndRetrieveAllSubjects() {
        val response = localDataSource.observeSubjects().getOrAwaitValue()
        assertThat(response, `is`(TestObjectUtil.subjects))
        assertThat(response.size, `is`(TestObjectUtil.subjects.size))
    }

    @Test
    fun saveRecentViewsAndRetrieveLess() {
        val response = localDataSource.observeRecentViews(2).getOrAwaitValue()
        assertThat(response, `is`(TestObjectUtil.recentViews.take(2)))
        assertThat(response.size, `is`(TestObjectUtil.recentViews.take(2).size))
    }

    @Test
    fun saveRecentViewsAndRetrieveAll() {
        val response = localDataSource.observeRecentViews(1000).getOrAwaitValue()
        assertThat(response, `is`(TestObjectUtil.recentViews))
        assertThat(response.size, `is`(TestObjectUtil.recentViews.size))
    }

    private fun saveSubjects() = runBlocking {
        localDataSource.saveSubjects(TestObjectUtil.subjects)
    }

    private fun saveReceiverViews() = runBlocking {
        localDataSource.saveRecentView(TestObjectUtil.recentViews[0])
        localDataSource.saveRecentView(TestObjectUtil.recentViews[1])
        localDataSource.saveRecentView(TestObjectUtil.recentViews[2])
        localDataSource.saveRecentView(TestObjectUtil.recentViews[3])
    }

    @After
    fun closeDb() {
        database.close()
    }
}
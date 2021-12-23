package com.example.ulesson.data

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.ulesson.data.helper.Resource
import com.example.ulesson.data.model.RecentView
import com.example.ulesson.data.model.Subject
import com.example.ulesson.data.source.local.SubjectLocalDataSource
import com.example.ulesson.data.source.remote.SubjectRemoteDataSource
import com.example.ulesson.data.source.repo.DefaultRepository
import com.example.ulesson.util.*
import com.nhaarman.mockitokotlin2.argumentCaptor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@ExperimentalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class RepositoryTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: DefaultRepository
    private val remoteDataSource = mock(SubjectRemoteDataSource::class.java)
    private val localDataSource = mock(SubjectLocalDataSource::class.java)

    @Before
    fun setup() {
        repository = DefaultRepository(remoteDataSource, localDataSource)
    }

    @Test
    fun `assert that call to network returns success`() = mainCoroutineRule.runBlockingTest {

        `when`(remoteDataSource.getSubjectData()).thenReturn(Resource.Success(TestObjectUtil.subjectDataResponse))

        val response = repository.fetchSubjects()

        verify(remoteDataSource).getSubjectData()
        assertTrue(response is Resource.Success)
    }

    @Test
    fun `assert that when call to network fails it returns the appropriate error message`() =
        mainCoroutineRule.runBlockingTest {
            `when`(remoteDataSource.getSubjectData()).thenReturn(Resource.Error("error occurred"))

            val response = repository.fetchSubjects()

            verify(remoteDataSource).getSubjectData()
            assertTrue(response is Resource.Error)
            verifyNoMoreInteractions(remoteDataSource, localDataSource)
        }

    //verify data passed using argument captor
    @Test
    fun `assert that when call to network is successful it should also persist the data gotten`() =
        mainCoroutineRule.runBlockingTest {
            `when`(remoteDataSource.getSubjectData()).thenReturn(Resource.Success(TestObjectUtil.subjectDataResponse))
            `when`(localDataSource.saveSubjects(TestObjectUtil.subjects)).thenReturn(Unit)

            val response = repository.fetchSubjects()

            assertTrue(response is Resource.Success)
            verify(remoteDataSource).getSubjectData()
            verify(localDataSource).saveSubjects(TestObjectUtil.subjects)
        }

    @Test
    fun `assert that repository gets subjects`() {
        val data = MutableLiveData<List<Subject>>()
        data.value = TestObjectUtil.subjects

        `when`(localDataSource.observeSubjects()).thenReturn(data)

        val response = repository.getSubjects().getOrAwaitValue()
        verify(localDataSource).observeSubjects()
        assertThat(response, `is`(TestObjectUtil.subjects))
    }

    @Test
    fun `assert that repository saves subjects`() = mainCoroutineRule.runBlockingTest {
        val argumentCapture = argumentCaptor<List<Subject>>()

        repository.saveSubjects(TestObjectUtil.subjects)
        verify(localDataSource).saveSubjects(argumentCapture.capture())

        val subjects = argumentCapture.firstValue
        assertThat(subjects, `is`(TestObjectUtil.subjects))
    }

    @Test
    fun `assert that repository saves recent view`() = mainCoroutineRule.runBlockingTest {
        val argumentCapture = argumentCaptor<RecentView>()

        repository.saveRecentView(TestObjectUtil.recentViews[0])
        verify(localDataSource).saveRecentView(argumentCapture.capture())

        val recentView = argumentCapture.firstValue
        assertThat(recentView, `is`(TestObjectUtil.recentViews[0]))
    }

    @Test
    fun `assert that repository retrieves recent views`() {
        val data = MutableLiveData<List<RecentView>>()
        data.value = TestObjectUtil.recentViews.take(2)

        `when`(localDataSource.observeRecentViews(2)).thenReturn(data)

        val response = repository.getRecentViews(2).getOrAwaitValue()
        verify(localDataSource).observeRecentViews(2)
        assertThat(response, `is`(TestObjectUtil.recentViews.take(2)))
    }

    @Test
    fun `assert that repository retrieves a subject, given the subject id`() =
        mainCoroutineRule.runBlockingTest {
            val data = TestObjectUtil.subjects[0]

            `when`(localDataSource.getSubject(3L)).thenReturn(Resource.Success(data))

            val response = repository.getSubject(3L)
            verify(localDataSource).getSubject(3L)
            assertTrue(response is Resource.Success)
        }

}
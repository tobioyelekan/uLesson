package com.example.ulesson.ui.subject

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.ulesson.data.FakeRepository
import com.example.ulesson.data.helper.Event
import com.example.ulesson.data.helper.Resource
import com.example.ulesson.data.model.RecentView
import com.example.ulesson.util.MainCoroutineRule
import com.example.ulesson.util.TestObjectUtil
import com.example.ulesson.util.getOrAwaitValue
import com.example.ulesson.util.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class SubjectViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: SubjectViewModel

    private val repository = FakeRepository()

    @Before
    fun init() {
        runBlocking {
            repository.saveSubjects(TestObjectUtil.subjects)
        }
        viewModel = SubjectViewModel(repository)
    }

    @Test
    fun `assert that call to get subject receives subject data`() =
        mainCoroutineRule.runBlockingTest {
            val subjectIdToOpen = TestObjectUtil.subjects[0].id
            val subject = viewModel.getSubject(subjectIdToOpen).getOrAwaitValue()
            assertTrue(subject is Resource.Success)
            assertThat((subject as Resource.Success).data, `is`(TestObjectUtil.subjects[0]))
        }

    @Test
    fun `assert that call to open a video sets navigateToVideo live data when subject is set`() {
        val subject = TestObjectUtil.subjects[0]
        val lesson = TestObjectUtil.lessons[0]

        viewModel.setSubject(subject)
        viewModel.openVideo(lesson)

        val topicName = subject.chapters.find { it.id == lesson.chapterId }!!.name

        val videoDataToOpen = viewModel.navigateToVideo.getOrAwaitValue().getContentIfNotHandled()
        assertThat(
            videoDataToOpen,
            `is`(RecentView(subject.id, subject.name, topicName, lesson.mediaUrl))
        )
    }

    @Test
    fun `assert that call to open a video doesn't set navigateToVideo live data without setting subject`() {

        val observer = mock<Observer<Event<RecentView>>>()

        //   val subject = TestObjectUtil.subjects[0]
        //   viewModel.setSubject(subject)
        val lesson = TestObjectUtil.lessons[0]
        viewModel.openVideo(lesson)

        verifyNoMoreInteractions(observer)

    }
}
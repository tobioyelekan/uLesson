package com.example.ulesson.ui.dashboard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ulesson.data.FakeRepository
import com.example.ulesson.data.helper.Resource
import com.example.ulesson.util.MainCoroutineRule
import com.example.ulesson.util.TestObjectUtil
import com.example.ulesson.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DashboardViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutine = MainCoroutineRule()

    private lateinit var viewModel: DashboardViewModel

    private val repository = FakeRepository()

    @Before
    fun init() {
        viewModel = DashboardViewModel(repository)
    }

    @Test
    fun `assert that call to network passes test`() {
        mainCoroutine.runBlockingTest {
            viewModel.getSubjects()

            val status = viewModel.fetchingSubject.getOrAwaitValue()
            assertThat(status, `is`(Resource.success(Unit)))
        }
    }

    @Test
    fun `assert that error response is received when error occurs calling network`() {
        mainCoroutine.runBlockingTest {
            repository.setShouldReturnError(true)
            viewModel.getSubjects()

            val status = viewModel.fetchingSubject.getOrAwaitValue()
            assertThat(status, `is`(Resource.error("error occurred")))
        }
    }

    @Test
    fun `assert that call to network saves subject data`() {
        mainCoroutine.runBlockingTest {
            viewModel.getSubjects()

            val status = viewModel.fetchingSubject.getOrAwaitValue()
            assertThat(status, `is`(Resource.success(Unit)))

            val subjects = viewModel.subjects.getOrAwaitValue()
            assertThat(subjects, `is`(TestObjectUtil.subjects))
        }
    }

    @Test
    fun `assert that clicking view all button changes the button text to view less`() {
        //initial state of button
        viewModel.toggleButton("VIEW ALL")

        val textBtn = viewModel.toggleText.getOrAwaitValue()
        assertThat(textBtn, `is`("VIEW LESS"))
    }

    @Test
    fun `assert that clicking view less button changes the button text to view all`() {
        //initial state of button
        viewModel.toggleButton("VIEW LESS")

        val textBtn = viewModel.toggleText.getOrAwaitValue()
        assertThat(textBtn, `is`("VIEW ALL"))
    }

    @Test
    fun `assert that clicking view less button receives two(2) list of recent watched topics`() {
        repository.saveAllRecentView(TestObjectUtil.recentViews)

        viewModel.toggleButton("VIEW LESS")

        val recentViews = viewModel.recentViews.getOrAwaitValue()
        assertThat(recentViews.size, `is`(2))
        assertThat(recentViews, `is`(TestObjectUtil.recentViews.take(2)))
    }

    @Test
    fun `assert that clicking view all button receives all the list of recent watched topics`() {
        repository.saveAllRecentView(TestObjectUtil.recentViews)

        viewModel.toggleButton("VIEW ALL")

        val recentViews = viewModel.recentViews.getOrAwaitValue()
        assertThat(recentViews.size, `is`(TestObjectUtil.recentViews.size))
        assertThat(recentViews, `is`(TestObjectUtil.recentViews))
    }

    @Test
    fun `assert that open subject receives sets livedata`() {
        val subjectIdToOpen = TestObjectUtil.subjects[0].id
        viewModel.openSubject(subjectIdToOpen)

        val subjectId = viewModel.openSubjectId.getOrAwaitValue().getContentIfNotHandled()
        assertThat(subjectId, `is`(notNullValue()))
        assertThat(subjectId, `is`(subjectIdToOpen))
    }
}
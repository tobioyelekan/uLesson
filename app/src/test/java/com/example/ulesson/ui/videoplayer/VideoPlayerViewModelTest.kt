package com.example.ulesson.ui.videoplayer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ulesson.data.FakeRepository
import com.example.ulesson.data.model.RecentView
import com.example.ulesson.util.MainCoroutineRule
import com.example.ulesson.util.TestObjectUtil
import com.example.ulesson.util.getOrAwaitValue
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.hamcrest.MatcherAssert.assertThat

@ExperimentalCoroutinesApi
class VideoPlayerViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val repository = FakeRepository()

    private lateinit var viewModel: VideoPlayerViewModel

    @Before
    fun init() {
        viewModel = VideoPlayerViewModel(repository)
    }

    @Test
    fun `assert that add to recent view passes`() = mainCoroutineRule.runBlockingTest {

        val recentView = RecentView(100L, "Android Tutorial", "Testing 101")
        viewModel.addRecentView(recentView)

        val recentViews = repository.getRecentViews(1000).getOrAwaitValue()
        assertThat(recentViews, `is`(not(TestObjectUtil.recentViews)))
        assertTrue(recentViews.size == 1)
    }

}
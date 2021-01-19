package com.example.ulesson.data.source.local

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.ulesson.data.model.RecentView
import com.example.ulesson.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class RecentViewDaoTest : DbSetup() {

    @Test
    fun insertAndRetrieve() = runBlocking {
        val singleRecentView = RecentView(15L, "Physics", "Gravity")
        recentDao.saveRecentView(singleRecentView)

        val recentViews = recentDao.getRecentViews(2).getOrAwaitValue()
        assertThat(recentViews.size, `is`(1))
        assertThat(recentViews[0], `is`(singleRecentView))
    }

    @Test
    fun insertManyAndRetrieve() = runBlocking {
        val recentView1 = RecentView(1L, "Physics", "Gravity")
        val recentView2 = RecentView(2L, "Biology", "Osmosis")
        val recentView3 = RecentView(3L, "Chemistry", "Gravity")
        val recentView4 = RecentView(4L, "Mathematics", "Trig")
        val recentView5 = RecentView(5L, "English", "Vocabulary")

        val allRecentViews = listOf(recentView1, recentView2, recentView3, recentView4, recentView5)

        recentDao.saveRecentView(recentView1)
        recentDao.saveRecentView(recentView2)
        recentDao.saveRecentView(recentView3)
        recentDao.saveRecentView(recentView4)
        recentDao.saveRecentView(recentView5)

        //view less
        val recentViewsViewLess = recentDao.getRecentViews(2).getOrAwaitValue()
        assertThat(recentViewsViewLess.size, `is`(2))
        assertThat(recentViewsViewLess, `is`(allRecentViews.take(2)))

        //view more
        val recentViewsViewMore = recentDao.getRecentViews(10).getOrAwaitValue()
        assertThat(recentViewsViewMore.size, `is`(allRecentViews.size))
        assertThat(recentViewsViewMore, `is`(allRecentViews.take(10)))
    }
}
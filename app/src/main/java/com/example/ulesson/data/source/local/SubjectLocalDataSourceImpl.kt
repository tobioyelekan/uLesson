package com.example.ulesson.data.source.local

import androidx.lifecycle.LiveData
import com.example.ulesson.data.helper.Resource
import com.example.ulesson.data.model.RecentView
import com.example.ulesson.data.model.Subject
import com.example.ulesson.data.source.local.dao.RecentViewDao
import com.example.ulesson.data.source.local.dao.SubjectDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SubjectLocalDataSourceImpl @Inject constructor(
    private val subjectDao: SubjectDao,
    private val recentViewDao: RecentViewDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : SubjectLocalDataSource {

    override suspend fun saveSubjects(subjects: List<Subject>) = withContext(ioDispatcher) {
        subjectDao.saveSubjects(subjects)
    }

    override suspend fun saveRecentView(recentView: RecentView) = withContext(ioDispatcher) {
        recentViewDao.saveRecentView(recentView)
    }

    override fun observeSubjects() = subjectDao.getAllSubjects()

    override suspend fun getSubject(id: Long) = withContext(ioDispatcher) {
        val subject = subjectDao.getSubject(id)
        return@withContext Resource.success(subject)
    }

    override fun observeRecentViews(limit: Int) = recentViewDao.getRecentViews(limit)

}
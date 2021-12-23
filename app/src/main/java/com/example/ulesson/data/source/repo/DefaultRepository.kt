package com.example.ulesson.data.source.repo

import com.example.ulesson.data.helper.Resource
import com.example.ulesson.data.helper.Resource.*
import com.example.ulesson.data.model.RecentView
import com.example.ulesson.data.model.Subject
import com.example.ulesson.data.source.local.SubjectLocalDataSource
import com.example.ulesson.data.source.remote.SubjectRemoteDataSource

class DefaultRepository(
    private val remoteDataSource: SubjectRemoteDataSource,
    private val localDataSource: SubjectLocalDataSource
) : Repository {

    override suspend fun fetchSubjects(): Resource<Unit> {
        return when (val response = remoteDataSource.getSubjectData()) {
            is Success -> {
                response.data?.let { subjectData ->
                    localDataSource.saveSubjects(subjectData.data.subjects)
                }
                Success(Unit)
            }
            is Error -> Error(response.message)
            is Loading -> Loading
        }
    }

    override fun getSubjects() = localDataSource.observeSubjects()

    override suspend fun saveRecentView(recentView: RecentView) {
        localDataSource.saveRecentView(recentView)
    }

    override fun getRecentViews(limit: Int) = localDataSource.observeRecentViews(limit)

    override suspend fun saveSubjects(subjects: List<Subject>) {
        localDataSource.saveSubjects(subjects)
    }

    override suspend fun getSubject(id: Long): Resource<Subject> {
        return localDataSource.getSubject(id)
    }

}
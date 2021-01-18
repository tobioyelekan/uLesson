package com.example.ulesson.data.source.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.ulesson.data.helper.Resource
import com.example.ulesson.data.helper.Resource.*
import com.example.ulesson.data.model.RecentView
import com.example.ulesson.data.model.Subject
import com.example.ulesson.data.source.local.SubjectLocalDataSource
import com.example.ulesson.data.source.remote.SubjectRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultRepository(
    private val remoteDataSource: SubjectRemoteDataSource,
    private val localDataSource: SubjectLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : Repository {

    override fun fetchSubjects(): LiveData<Resource<Unit>> =
        liveData(ioDispatcher) {
            val response = remoteDataSource.getSubjectData()
            when (response.status) {
                Status.LOADING -> {
                    emit(Resource.loading((Unit)))
                }
                Status.SUCCESS -> {
                    response.data?.let { subjectData ->
                        localDataSource.saveSubjects(subjectData.data.subjects)
                    }
                    emit(Resource.success(Unit))
                }
                Status.ERROR -> {
                    emit(Resource.error(response.message!!, null))
                }
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
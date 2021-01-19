package com.example.ulesson.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.ulesson.data.helper.Resource
import com.example.ulesson.data.helper.Resource.Status
import com.example.ulesson.data.model.RecentView
import com.example.ulesson.data.model.Subject
import com.example.ulesson.data.source.repo.Repository
import com.example.ulesson.util.TestObjectUtil

class FakeRepository : Repository {

    private var shouldReturnError = false

    private val subjects = mutableListOf<Subject>()
    private val recentviews = mutableListOf<RecentView>()

    private val observeSubjects = MutableLiveData<List<Subject>>()
    private val observeRecentViews = MutableLiveData<List<RecentView>>()

    fun setShouldReturnError(value: Boolean) {
        shouldReturnError = value
    }

    //network call to fetch subjects from the server
    override fun fetchSubjects(): LiveData<Resource<Unit>> {
        return liveData {
            val response = if (shouldReturnError) {
                Resource.error("error occurred", null)
            } else {
                Resource.success(TestObjectUtil.subjects)
            }

            when (response.status) {
                Status.SUCCESS -> {
                    response.data?.let {
                        saveSubjects(it)
                    }
                    emit(Resource.success(Unit))
                }
                Status.ERROR -> {
                    emit(Resource.error(response.message!!, null))
                }
                else -> {
                }
            }
        }
    }

    override fun getSubjects(): LiveData<List<Subject>> {
        return observeSubjects
    }

    override suspend fun saveRecentView(recentView: RecentView) {
        this.recentviews.add(recentView)
        refreshData()
    }

    override fun getRecentViews(limit: Int): LiveData<List<RecentView>> {
        observeRecentViews.value = recentviews.take(limit)
        return observeRecentViews
    }

    override suspend fun saveSubjects(subjects: List<Subject>) {
        this.subjects.addAll(subjects)
        refreshData()
    }

    override suspend fun getSubject(id: Long): Resource<Subject> {
        val subject = subjects.find { it.id == id }
        return Resource.success(subject!!)
    }

    fun saveAllRecentView(list: List<RecentView>) {
        recentviews.addAll(list)
        refreshData()
    }

    private fun refreshData() {
        observeSubjects.value = subjects
        observeRecentViews.value = recentviews
    }

}
package com.example.ulesson.data.source.repo

import androidx.lifecycle.LiveData
import com.example.ulesson.data.helper.Resource
import com.example.ulesson.data.model.RecentView
import com.example.ulesson.data.model.Subject

interface Repository {

    suspend fun fetchSubjects(): Resource<Unit>

    fun getSubjects(): LiveData<List<Subject>>

    suspend fun saveRecentView(recentView: RecentView)

    fun getRecentViews(limit: Int): LiveData<List<RecentView>>

    suspend fun saveSubjects(subjects: List<Subject>)

    suspend fun getSubject(id: Long): Resource<Subject>
}
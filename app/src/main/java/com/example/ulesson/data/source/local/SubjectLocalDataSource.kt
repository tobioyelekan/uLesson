package com.example.ulesson.data.source.local

import androidx.lifecycle.LiveData
import com.example.ulesson.data.helper.Resource
import com.example.ulesson.data.model.RecentView
import com.example.ulesson.data.model.Subject

interface SubjectLocalDataSource {
    suspend fun saveSubjects(subjects: List<Subject>)

    suspend fun saveRecentView(recentView: RecentView)

    fun observeSubjects(): LiveData<List<Subject>>

    suspend fun getSubject(id: Long): Resource<Subject>

    fun observeRecentViews(limit: Int): LiveData<List<RecentView>>

}
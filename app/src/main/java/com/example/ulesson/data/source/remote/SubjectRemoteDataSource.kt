package com.example.ulesson.data.source.remote

import com.example.ulesson.data.helper.Resource
import com.example.ulesson.data.model.SubjectDataResponse

interface SubjectRemoteDataSource {
    suspend fun getSubjectData(): Resource<SubjectDataResponse>
}
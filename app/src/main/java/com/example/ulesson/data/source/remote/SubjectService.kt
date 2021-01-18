package com.example.ulesson.data.source.remote

import com.example.ulesson.data.model.SubjectDataResponse
import retrofit2.Response
import retrofit2.http.GET

interface SubjectService {
    @GET("content/grade")
    suspend fun getSubjectData(): Response<SubjectDataResponse>
}
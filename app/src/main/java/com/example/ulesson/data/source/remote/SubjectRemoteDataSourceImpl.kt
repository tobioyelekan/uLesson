package com.example.ulesson.data.source.remote

import com.example.ulesson.data.helper.Resource
import com.example.ulesson.data.model.SubjectDataResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class SubjectRemoteDataSourceImpl @Inject constructor(
    private val subjectService: SubjectService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : SubjectRemoteDataSource {
    override suspend fun getSubjectData(): Resource<SubjectDataResponse> =
        withContext(ioDispatcher) {
            return@withContext try {
                val response = subjectService.getSubjectData()
                if (response.isSuccessful) {
                    Resource.Success(response.body()!!)
                } else {
                    Resource.Error(response.errorBody().toString(), null)
                }
            } catch (e: Exception) {
                Resource.Error(e.message!!, null)
            } catch (IO: Exception) {
                Resource.Error("No internet access", null)
            }
        }
}
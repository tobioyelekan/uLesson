package com.example.ulesson.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ulesson.data.model.Subject

@Dao
interface SubjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSubjects(subjects: List<Subject>)

    @Query("SELECT * FROM subject")
    fun getAllSubjects(): LiveData<List<Subject>>

    @Query("SELECT * FROM subject WHERE id =:id")
    suspend fun getSubject(id: Long): Subject
}
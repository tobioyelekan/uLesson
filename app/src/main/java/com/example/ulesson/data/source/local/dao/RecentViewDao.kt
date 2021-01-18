package com.example.ulesson.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ulesson.data.model.RecentView

@Dao
interface RecentViewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRecentView(recentView: RecentView)

    @Query("SELECT * FROM recentview LIMIT :num")
    fun getRecentViews(num: Int): LiveData<List<RecentView>>
}
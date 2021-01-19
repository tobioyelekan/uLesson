package com.example.ulesson.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.example.ulesson.data.source.local.dao.RecentViewDao
import com.example.ulesson.data.source.local.dao.SubjectDao
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class DbSetup {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: ULessonDatabase
    protected lateinit var subjectDao: SubjectDao
    protected lateinit var recentDao: RecentViewDao

    @Before
    fun init() {
        db = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            ULessonDatabase::class.java
        ).build()

        subjectDao = db.subjectDao()
        recentDao = db.recentViewDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

}
package com.example.ulesson.data.source.local

import android.content.Context
import androidx.room.*
import com.example.ulesson.data.model.Chapter
import com.example.ulesson.data.model.RecentView
import com.example.ulesson.data.model.Subject
import com.example.ulesson.data.source.local.dao.RecentViewDao
import com.example.ulesson.data.source.local.dao.SubjectDao
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Database(
    entities = [Subject::class, RecentView::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(Converter::class)
abstract class ULessonDatabase : RoomDatabase() {

    abstract fun subjectDao(): SubjectDao
    abstract fun recentViewDao(): RecentViewDao

    companion object {
        private var instance: ULessonDatabase? = null

        fun getDatabase(context: Context): ULessonDatabase {
            if (instance == null) {
                synchronized(ULessonDatabase::class.java) {}
                instance =
                    Room.databaseBuilder(context, ULessonDatabase::class.java, "AppDatabase")
                        .fallbackToDestructiveMigration()
                        .build()
            }

            return instance!!
        }
    }
}

class Converter {
    @TypeConverter
    fun fromChapterList(chapterList: List<Chapter>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Chapter>>() {}.type
        return gson.toJson(chapterList, type)
    }

    @TypeConverter
    fun toChapterList(chapterStringList: String): List<Chapter> {
        val gson = Gson()
        val type = object : TypeToken<List<Chapter>>() {}.type
        return gson.fromJson(chapterStringList, type)
    }
}
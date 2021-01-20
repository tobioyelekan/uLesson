package com.example.ulesson.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class SubjectDataResponse(
    val data: SubjectData
)

data class SubjectData(
    val status: Boolean,
    val message: Boolean,
    val subjects: List<Subject>
)

@Entity(tableName = "subject")
data class Subject(
    @PrimaryKey
    val id: Long,
    val name: String,
    val icon: String,
    var chapters: List<Chapter>
)

data class Chapter(
    val id: Long,
    val name: String,
    val lessons: List<Lesson>
)

@Entity(tableName = "recentview")
data class RecentView @JvmOverloads constructor(
    @PrimaryKey
    val subjectId: Long,
    val subjectName: String,
    val topicName: String,

    /**
     * I used @Ignore here, because its not needed in the recently watched videos,
     * therefore does not need to be persisted.
     * @property mediaUrl is used here for passing it as an extra parameter to the video player fragment
     */
    @Ignore
    val mediaUrl: String = ""
)

data class Lesson(
    val id: Long,
    val name: String,
    val icon: String,
    @SerializedName("media_url")
    val mediaUrl: String,
    @SerializedName("subject_id")
    val subjectId: Long,
    @SerializedName("chapter_id")
    val chapterId: Long
)
package com.example.ulesson.ui.subject

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.ulesson.data.helper.Event
import com.example.ulesson.data.helper.Resource
import com.example.ulesson.data.model.Lesson
import com.example.ulesson.data.model.RecentView
import com.example.ulesson.data.model.Subject
import com.example.ulesson.data.source.repo.Repository

class SubjectViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {

    private var subject: Subject? = null

    private val _navigateToVideo = MutableLiveData<Event<RecentView>>()
    val navigateToVideo: LiveData<Event<RecentView>> = _navigateToVideo

    fun getSubject(id: Long): LiveData<Resource<Subject>> =
        liveData {
            emit(repository.getSubject(id))
        }

    fun setSubject(subject: Subject) {
        this.subject = subject
    }

    fun openVideo(lesson: Lesson) {
        subject?.let { subject ->
            val topicName = subject.chapters.find { it.id == lesson.chapterId }!!.name
            _navigateToVideo.value = Event(
                RecentView(subject.id, subject.name, topicName, lesson.mediaUrl)
            )
        }
    }
}

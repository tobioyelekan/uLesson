package com.example.ulesson.ui.dashboard

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.ulesson.data.helper.Event
import com.example.ulesson.data.helper.Resource
import com.example.ulesson.data.source.repo.Repository
import kotlinx.coroutines.launch

class DashboardViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _fetchingSubject = MutableLiveData<Resource<Unit>>()
    val fetchingSubject: LiveData<Resource<Unit>> = _fetchingSubject

    init {
        fetchSubjects()
    }

    fun fetchSubjects() {
        _fetchingSubject.value = Resource.Loading

        viewModelScope.launch {
            _fetchingSubject.value = repository.fetchSubjects()
        }
    }

    private val _openSubjectId = MutableLiveData<Event<Long>>()
    val openSubjectId: LiveData<Event<Long>> = _openSubjectId

    private val _toggleText = MutableLiveData<String>()
    val toggleText: LiveData<String> = _toggleText

    val recentViews = toggleText.switchMap {
        when (it) {
            "VIEW ALL" -> repository.getRecentViews(2)
            else -> repository.getRecentViews(1000)
        }
    }

    fun toggleButton(text: String) {
        when (text) {
            "VIEW ALL" -> {
                _toggleText.value = "VIEW LESS"
            }
            "VIEW LESS" -> {
                _toggleText.value = "VIEW ALL"
            }
        }
    }

    fun openSubject(subjectId: Long) {
        _openSubjectId.value = Event(subjectId)
    }

    val subjects = repository.getSubjects()

}
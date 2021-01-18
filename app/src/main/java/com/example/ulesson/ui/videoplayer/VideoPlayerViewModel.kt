package com.example.ulesson.ui.videoplayer

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ulesson.data.model.RecentView
import com.example.ulesson.data.source.repo.Repository
import kotlinx.coroutines.launch

class VideoPlayerViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {

    fun addRecentView(recentView: RecentView) {
        viewModelScope.launch {
            repository.saveRecentView(recentView)
        }
    }
}
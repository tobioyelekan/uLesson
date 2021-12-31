package com.example.ulesson.ui.videoplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ulesson.data.model.RecentView
import com.example.ulesson.data.source.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    fun addRecentView(recentView: RecentView) {
        viewModelScope.launch {
            repository.saveRecentView(recentView)
        }
    }
}
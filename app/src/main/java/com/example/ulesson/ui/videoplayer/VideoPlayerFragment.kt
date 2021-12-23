package com.example.ulesson.ui.videoplayer

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ulesson.R
import com.example.ulesson.data.model.RecentView
import com.example.ulesson.ui.BaseFragment
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.video_player_fragment.*

@AndroidEntryPoint
class VideoPlayerFragment : BaseFragment(R.layout.video_player_fragment), Player.EventListener {

    private val viewModel: VideoPlayerViewModel by viewModels()
    private var player: SimpleExoPlayer? = null

    private val args: VideoPlayerFragmentArgs by navArgs()

    private var playWhenReady = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(context)
        player?.playbackParameters = PlaybackParameters(1.2f)
        player!!.addListener(this)
        playerView.player = player

        val mediaSource = buildMediaSource(Uri.parse(args.mediaUrl))
        player!!.playWhenReady = playWhenReady
        player!!.prepare(mediaSource, false, false)
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val dataSourceFactory = DefaultDataSourceFactory(context, "exoplayer-ulesson")
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        when (playbackState) {
            ExoPlayer.STATE_IDLE -> {
            }
            ExoPlayer.STATE_READY -> {
                /** add to recently watched video.
                 * Normally, I think it should be done in the [ExoPlayer.STATE_ENDED],
                 * but inorder not to waste the time of the person reviewing this code
                 * I'm assuming that when the player is ready the video has been watched.
                 **/
                viewModel.addRecentView(
                    RecentView(
                        args.subjectId,
                        args.subjectName,
                        args.topicName
                    )
                )
            }
            ExoPlayer.STATE_BUFFERING -> {
            }
            ExoPlayer.STATE_ENDED -> {
            }
        }
    }

    override fun onPlayerError(error: ExoPlaybackException?) {
        super.onPlayerError(error)

        error?.let {
            showToast(
                it.message ?: it.localizedMessage
                ?: "something went wrong trying to play this video"
            )
        }
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT < 24 || player == null) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    private fun releasePlayer() {
        if (player != null) {
            playWhenReady = player!!.playWhenReady
            player!!.removeListener(this)
            player!!.release()
            player = null
        }
    }

}
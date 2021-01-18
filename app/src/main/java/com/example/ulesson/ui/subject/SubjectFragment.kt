package com.example.ulesson.ui.subject

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ulesson.R
import com.example.ulesson.data.helper.EventObserver
import com.example.ulesson.ui.BaseFragment
import com.example.ulesson.ui.subject.adapters.ChapterAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.subject_fragment.*

@AndroidEntryPoint
class SubjectFragment : BaseFragment(R.layout.subject_fragment) {

    private val args: SubjectFragmentArgs by navArgs()
    private val viewModel: SubjectViewModel by viewModels()

    private lateinit var adapter: ChapterAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter =
            ChapterAdapter(viewModel)
        chapterList.adapter = adapter

        back.setOnClickListener {
            findNavController().navigateUp()
        }

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.getSubject(args.subjectId).observe(viewLifecycleOwner, Observer {
            loading.visibility = View.GONE
            it.data?.let { subject ->
                viewModel.subject = subject
                subjectTitle.text = subject.name
                adapter.submitList(subject.chapters)
            }
        })

        viewModel.navigateToVideo.observe(viewLifecycleOwner, EventObserver {
            val action = SubjectFragmentDirections
                .subjectToVideoPlayerFragment(
                    it.mediaUrl,
                    it.subjectId,
                    it.subjectName,
                    it.topicName
                )
            findNavController().navigate(action)
        })
    }

}
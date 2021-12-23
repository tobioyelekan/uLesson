package com.example.ulesson.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ulesson.R
import com.example.ulesson.data.helper.EventObserver
import com.example.ulesson.data.helper.Resource.*
import com.example.ulesson.ui.BaseFragment
import com.example.ulesson.ui.dashboard.adapters.RecentViewAdapter
import com.example.ulesson.ui.dashboard.adapters.SubjectAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dashboard_fragment.*

@AndroidEntryPoint
class DashboardFragment : BaseFragment(R.layout.dashboard_fragment) {

    private val viewModel: DashboardViewModel by viewModels()
    private lateinit var adapter: SubjectAdapter
    private lateinit var recentViewAdapter: RecentViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SubjectAdapter(viewModel)
        recentViewAdapter = RecentViewAdapter()

        subjectList.adapter = adapter
        recentViewList.adapter = recentViewAdapter

        subjectList.layoutManager = GridLayoutManager(requireContext(), 2)

        setupObservers()
        setUpToggleViewClickListener()
        viewModel.toggleButton(toggleViewText.text.toString())
    }

    private fun setUpToggleViewClickListener() {
        toggleBtn.setOnClickListener {
            viewModel.toggleButton(toggleViewText.text.toString())
        }
    }

    private fun setupObservers() {
        viewModel.fetchingSubject.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Loading -> {
                    loading.visibility = View.VISIBLE
                }
                is Success -> {
                    loading.visibility = View.GONE
                }
                is Error -> {
                    loading.visibility = View.GONE
                    onError(it.message)
                }
            }
        })

        viewModel.subjects.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                adapter.submitList(it)
            }
        })

        viewModel.openSubjectId.observe(viewLifecycleOwner, EventObserver {
            val action = DashboardFragmentDirections.dashboardToSubjectFragment(it)
            findNavController().navigate(action)
        })

        viewModel.recentViews.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                emptyRecent.visibility = View.GONE
                toggleViewContainer.visibility = View.VISIBLE
            }

            recentViewAdapter.submitList(it)
        })

        viewModel.toggleText.observe(viewLifecycleOwner, Observer {
            toggleViewText.text = it
        })
    }

}
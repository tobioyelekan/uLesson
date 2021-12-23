package com.example.ulesson.ui.dashboard.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ulesson.R
import com.example.ulesson.data.model.Subject
import com.example.ulesson.ui.dashboard.DashboardViewModel
import kotlinx.android.synthetic.main.subject_item.view.*

class SubjectAdapter(private val viewModel: DashboardViewModel) :
    ListAdapter<Subject, SubjectAdapter.ViewHolder>(
        SubjectDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            inflater.inflate(R.layout.subject_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModel, getItem(position))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(viewModel: DashboardViewModel, subject: Subject) {
            itemView.subjectTitle.text = subject.name
            with(itemView) {
                when (subject.name) {
                    "Mathematics" -> {
                        setContent(
                            itemView,
                            R.color.colorRed,
                            R.drawable.ic_maths_bg,
                            R.drawable.ic_maths_sign
                        )
                    }
                    "Physics" -> {
                        setContent(
                            itemView,
                            R.color.colorPurple,
                            R.drawable.ic_physics_bg,
                            R.drawable.ic_phy_sign
                        )
                    }
                    "English" -> {
                        setContent(
                            itemView,
                            R.color.colorPurpleDark,
                            R.drawable.ic_english_bg,
                            R.drawable.ic_eng_sign
                        )
                    }
                    "Biology" -> {
                        setContent(
                            itemView,
                            R.color.colorGreen,
                            R.drawable.ic_biology_bg,
                            R.drawable.ic_bio_sign
                        )
                    }
                    "Chemistry" -> {
                        setContent(
                            itemView,
                            R.color.colorOrange,
                            R.drawable.ic_chemistry_bg,
                            R.drawable.ic_chem_sign
                        )
                    }
                    else ->{
                        setContent(
                            itemView,
                            R.color.colorPurpleDark,
                            R.drawable.ic_english_bg,
                            R.drawable.ic_phy_sign
                        )
                    }
                }

                setOnClickListener {
                    viewModel.openSubject(subject.id)
                }
            }
        }

        private fun setContent(
            itemView: View,
            @ColorRes bgColor: Int,
            @DrawableRes bgImg: Int,
            @DrawableRes signImg: Int
        ) {
            itemView.subjectContainer.backgroundTintList =
                ContextCompat.getColorStateList(itemView.context, bgColor)
            itemView.bgImg.setImageDrawable(ContextCompat.getDrawable(itemView.context, bgImg))
            itemView.signImg.setImageDrawable(ContextCompat.getDrawable(itemView.context, signImg))
        }
    }
}

class SubjectDiffCallback : DiffUtil.ItemCallback<Subject>() {
    override fun areItemsTheSame(oldItem: Subject, newItem: Subject): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Subject, newItem: Subject): Boolean {
        return oldItem == newItem
    }
}
package com.pranksounds.funny.haircut.sound.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pranksounds.funny.haircut.sound.databinding.SlideItemContainerBinding
import com.pranksounds.funny.haircut.sound.models.IntroSlide

class IntroSlideAdapter(private val introSlides: List<IntroSlide>) :
    RecyclerView.Adapter<IntroSlideAdapter.ViewHolder>() {
    class ViewHolder(private val binding: SlideItemContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(introSlide: IntroSlide) {
            Glide.with(binding.root)
                .load(introSlide.icon)
                .centerCrop()
                .into(binding.imgSlideIcon)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        SlideItemContainerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = introSlides.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(introSlides[position])
    }
}
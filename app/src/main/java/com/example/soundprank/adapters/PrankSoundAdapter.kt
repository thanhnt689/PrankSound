package com.example.soundprank.adapters

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.soundprank.callback.OnClickItemSoundPrank
import com.example.soundprank.databinding.LayoutItemSoundPrankBinding
import com.example.soundprank.models.SoundPrank
import java.util.*


class PrankSoundAdapter(
    private var soundPranks: List<SoundPrank>,
    private val onClickItemSoundPrank: OnClickItemSoundPrank
) :
    RecyclerView.Adapter<PrankSoundAdapter.ViewHolder>() {
    class ViewHolder(val binding: LayoutItemSoundPrankBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(soundPrank: SoundPrank) {
            Glide.with(binding.imgSoundPrank)
                .load(soundPrank.image)
                .centerCrop()
                .into(binding.imgSoundPrank)

            binding.tvSoundPrank.isSelected = true

            binding.clSoundPrank.setBackgroundColor(soundPrank.colorBgText.toColorInt())
            binding.tvSoundPrank.text = soundPrank.name

            val background: Drawable = binding.imgSoundPrank.background
            val gradientDrawable = background as GradientDrawable
            gradientDrawable.setColor(soundPrank.colorBgImage.toColorInt());
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutItemSoundPrankBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(soundPranks[position])
        holder.binding.root.setOnClickListener {
            onClickItemSoundPrank.onClickItemSoundPrank(soundPranks[position], position)
        }
    }

    override fun getItemCount(): Int {
        return soundPranks.size
    }
}
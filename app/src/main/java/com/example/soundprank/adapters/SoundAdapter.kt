package com.example.soundprank.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.soundprank.callback.OnClickItemSound
import com.example.soundprank.databinding.LayoutItemSoundBinding
import com.example.soundprank.models.Sound

class SoundAdapter(
    private var sounds: ArrayList<Sound>,
    private val onClickItemSound: OnClickItemSound
) :
    RecyclerView.Adapter<SoundAdapter.ViewHolder>() {
    class ViewHolder(val binding: LayoutItemSoundBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(sound: Sound) {
            binding.tvSoundName.text = sound.name

            Glide.with(binding.imgSound)
                .load(sound.image)
                .centerCrop()
                .into(binding.imgSound)

            binding.tvSoundName.isSelected = true
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutItemSoundBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return sounds.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(sounds[position])
        holder.binding.root.setOnClickListener {
            onClickItemSound.onCLickItemSound(sounds[position])
        }
    }
}
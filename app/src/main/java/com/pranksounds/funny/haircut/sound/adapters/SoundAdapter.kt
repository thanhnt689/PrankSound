package com.pranksounds.funny.haircut.sound.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pranksounds.funny.haircut.sound.callback.OnClickItemSound
import com.pranksounds.funny.haircut.sound.databinding.LayoutItemSoundBinding
import com.pranksounds.funny.haircut.sound.models.Sound


class SoundAdapter(
    private val context: Context,
    private var sounds: ArrayList<Sound>,
    private val onClickItemSound: OnClickItemSound
) :
    RecyclerView.Adapter<SoundAdapter.ViewHolder>() {

    private var clicked = false

    inner class ViewHolder(val binding: LayoutItemSoundBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(sound: Sound) {

            binding.tvSoundName.text = "${context.getString(sound.idString)} ${sound.num}"
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
            if (clicked) {
                return@setOnClickListener
            }
            clicked = true
            it.handler.postDelayed({ clicked = false }, 500)

            onClickItemSound.onCLickItemSound(sounds[position])
        }
    }
}
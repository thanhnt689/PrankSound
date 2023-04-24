package com.example.soundprank.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.soundprank.callback.OnClickItemSoundFavourite
import com.example.soundprank.databinding.LayoutItemSoundFavouriteBinding
import com.example.soundprank.models.Sound

class SoundFavouriteAdapter(
    private val soundFavourites: ArrayList<Sound>,
    private val onClickItemSoundFavourite: OnClickItemSoundFavourite
) :
    RecyclerView.Adapter<SoundFavouriteAdapter.ViewHolder>() {
    class ViewHolder(val binding: LayoutItemSoundFavouriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
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
            LayoutItemSoundFavouriteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return soundFavourites.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(soundFavourites[position])
        holder.binding.checkBox.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                onClickItemSoundFavourite.onClickItemSoundFavourite(soundFavourites[position])
            }
        }
        holder.binding.root.setOnClickListener {

        }
    }

}
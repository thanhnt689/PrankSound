package com.pranksounds.funny.haircut.sound.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pranksounds.funny.haircut.sound.callback.OnClickCbSound
import com.pranksounds.funny.haircut.sound.callback.OnClickItemSound
import com.pranksounds.funny.haircut.sound.databinding.LayoutItemSoundFavouriteBinding
import com.pranksounds.funny.haircut.sound.models.Sound

class SoundFavouriteAdapter(
    private val context: Context,
    private val soundFavourites: ArrayList<Sound>,
    private val onClickItemSound: OnClickItemSound,
    private val onClickCbSound: OnClickCbSound
) :
    RecyclerView.Adapter<SoundFavouriteAdapter.ViewHolder>() {

    var showCheckBox = false

    var checkAll = false

    inner class ViewHolder(val binding: LayoutItemSoundFavouriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(sound: Sound) {
            //binding.tvSoundName.text = sound.name
            binding.tvSoundName.text = "${context.getString(sound.idString)} ${sound.num}"

            Glide.with(binding.imgSound)
                .load(sound.image)
                .centerCrop()
                .into(binding.imgSound)

            binding.tvSoundName.isSelected = true
            Log.d("ntt", "bind ${sound.isSelected}")
            binding.checkBox.isChecked = sound.isSelected

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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.tvSoundName.text =
            "${context.getString(soundFavourites[position].idString)} ${soundFavourites[position].num}"

        Glide.with(holder.binding.imgSound)
            .load(soundFavourites[position].image)
            .centerCrop()
            .into(holder.binding.imgSound)

        holder.binding.tvSoundName.isSelected = true

        holder.binding.checkBox.isChecked = soundFavourites[position].isSelected

        holder.binding.checkBox.setOnClickListener {
            holder.binding.checkBox.isChecked = soundFavourites[position].isSelected
        }

        holder.binding.checkBox.setOnClickListener {
            if (soundFavourites[position].isSelected) {
                onClickCbSound.onClickCbSound(false, soundFavourites[position])
            } else {
                onClickCbSound.onClickCbSound(true, soundFavourites[position])
            }
        }


        holder.binding.root.setOnClickListener {
            onClickItemSound.onCLickItemSound(soundFavourites[position])
        }

        if (showCheckBox) {
            holder.binding.checkBox.visibility = View.VISIBLE
        } else {
            holder.binding.checkBox.visibility = View.GONE
        }

    }

    fun setShowOrHideCheckBox(check: Boolean) {
        showCheckBox = check
        notifyDataSetChanged()
    }

    fun setCheckBoxAll(sCheckAll: Boolean) {
        checkAll = sCheckAll
        notifyDataSetChanged()
    }

}
package com.example.soundprank.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.soundprank.callback.OnClickCbSound
import com.example.soundprank.callback.OnClickItemSound
import com.example.soundprank.databinding.LayoutItemSoundFavouriteBinding
import com.example.soundprank.models.Sound

class SoundFavouriteAdapter(
    private val soundFavourites: ArrayList<Sound>,
    private val onClickItemSound: OnClickItemSound,
    private val onClickCbSound: OnClickCbSound
) :
    RecyclerView.Adapter<SoundFavouriteAdapter.ViewHolder>() {

    var showCheckBox = false

    var checkAll = false

    class ViewHolder(val binding: LayoutItemSoundFavouriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(sound: Sound) {
            binding.tvSoundName.text = sound.name

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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        //holder.binding.checkBox.isChecked = checkAll
//        holder.bind(soundFavourites[position])
        Log.d("ThanhNT", soundFavourites.toString())
        holder.binding.tvSoundName.text = soundFavourites[position].name

        Glide.with(holder.binding.imgSound)
            .load(soundFavourites[position].image)
            .centerCrop()
            .into(holder.binding.imgSound)

        holder.binding.tvSoundName.isSelected = true
        // Log.d("ntt", "bind ${sound.isSelected}")
        holder.binding.checkBox.isChecked = soundFavourites[position].isSelected

        //holder.binding.checkBox.isChecked = checkAll
        // holder.binding.checkBox.isChecked = soundFavourites[position].isSelected

        holder.binding.checkBox.setOnClickListener {
            holder.binding.checkBox.isChecked = soundFavourites[position].isSelected
        }


//        holder.binding.checkBox.setOnCheckedChangeListener { compoundButton, b ->
//
//            onClickCbSound.onClickCbSound(b, soundFavourites[position])
//
//           soundFavourites[position].isSelected = b
//
//        }

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
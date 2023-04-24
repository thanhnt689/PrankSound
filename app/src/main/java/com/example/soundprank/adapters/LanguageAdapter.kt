package com.example.soundprank.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.soundprank.callback.OnClickItemLanguage
import com.example.soundprank.databinding.LayoutItemLanguageBinding
import com.example.soundprank.models.Language


class LanguageAdapter(
    private var languages: List<Language>,
    private var onClickItemLanguage: OnClickItemLanguage
) : RecyclerView.Adapter<LanguageAdapter.ViewHolder>() {

    var selectedPosition = 0

    class ViewHolder(val binding: LayoutItemLanguageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(language: Language) {
            binding.tvLanguage.text = language.languageName
            Glide.with(binding.imgLanguage)
                .load(language.image)
                .centerCrop()
                .into(binding.imgLanguage)
            binding.rbLanguage.isChecked = language.isSelected
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutItemLanguageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return languages.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(languages[position])
        // holder.binding.rbLanguage.isChecked = position == selectedPosition
        holder.binding.rbLanguage.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                selectedPosition = holder.adapterPosition
                notifyDataSetChanged()
                onClickItemLanguage.onClick(languages[position])
            }
            holder.binding.rbLanguage.isChecked = selectedPosition == position
        }

        holder.binding.root.setOnClickListener {
            selectedPosition = holder.adapterPosition
            holder.binding.rbLanguage.isChecked = position == selectedPosition
            notifyDataSetChanged()
            onClickItemLanguage.onClick(languages[position])
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSelectLanguage(language: Language) {
        for (data in languages) {
            data.isSelected = data.languageName == language.languageName
        }
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
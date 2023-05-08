package com.pranksounds.funny.haircut.sound.adapters

import android.R
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pranksounds.funny.haircut.sound.callback.OnClickItemLanguage
import com.pranksounds.funny.haircut.sound.databinding.LayoutItemLanguageBinding
import com.pranksounds.funny.haircut.sound.models.Language


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

        holder.binding.rbLanguage.setOnClickListener {
            onClickItemLanguage.onClick(languages[position])
        }

        holder.binding.root.setOnClickListener {
            onClickItemLanguage.onClick(languages[position])
        }

        val colorStateList = ColorStateList(
            arrayOf<IntArray>(
                intArrayOf(R.attr.state_checked), intArrayOf()
            ), intArrayOf(
                Color.parseColor("#F5871A"),  //checked color
                Color.parseColor("#BEC7D8")  //normal color
            )
        )

        holder.binding.rbLanguage.buttonTintList = colorStateList

        holder.binding.rbLanguage.isChecked = languages[position].isSelected
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
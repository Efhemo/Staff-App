package com.efhem.mystaff.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.efhem.mystaff.R
import com.efhem.mystaff.utils.capitalin
import com.efhem.mystaff.model.Staff
import kotlinx.android.synthetic.main.staff_item.view.*

class StaffsAdapter :
    ListAdapter<Staff, StaffsAdapter.ItemViewholder>(StaffsAdapterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.staff_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: StaffsAdapter.ItemViewholder, position: Int) {
        holder.bind(getItem(position))
    }

    class ItemViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Staff) = with(itemView) {

            Glide.with(itemView.context).load(item.imageUrk)
                .apply(RequestOptions.placeholderOf(R.drawable.female_profile_2))
                .apply(RequestOptions.errorOf(R.drawable.female_profile_2))
                .apply(RequestOptions.centerCropTransform())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(this.image)

            this.fullname.text = capitalin(item.fullname)
            this.specialty.text = item.speciallty
            this.location.text = item.pob
            this.dob.text = item.dob

            setOnClickListener {

            }
        }
    }
}

class StaffsAdapterDiffCallback : DiffUtil.ItemCallback<Staff>() {
    override fun areItemsTheSame(oldItem: Staff, newItem: Staff): Boolean {
        return oldItem.email == newItem.email
    }

    override fun areContentsTheSame(oldItem: Staff, newItem: Staff): Boolean {
        return oldItem == newItem
    }
}
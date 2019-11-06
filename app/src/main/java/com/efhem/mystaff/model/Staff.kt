package com.efhem.mystaff.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Staff(val fullname: String, @PrimaryKey val email: String, val speciallty: String = "",
                 val password: String,
                 val dob: String, val pob: String, val imageUrk: String = "")
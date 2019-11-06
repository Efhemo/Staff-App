package com.efhem.mystaff.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.efhem.mystaff.model.Staff

@Dao
interface DaoProfile {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProfile(profile: Staff?): Long

    @Query("SELECT * FROM staff where email = :email")
    fun getProfileByEmail(email: String): Staff?

    @Query("SELECT * FROM staff")
    fun getStaffs(): LiveData<List<Staff>>

}
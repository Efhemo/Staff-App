package com.efhem.mystaff.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.efhem.mystaff.model.Staff


@Database(entities = [Staff::class], version = 1, exportSchema = false)
abstract class StaffDatabase : RoomDatabase() {
    abstract val daoProfile: DaoProfile

}

private lateinit var INSTANCE: StaffDatabase

fun database(context: Context): StaffDatabase {
    synchronized(StaffDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                StaffDatabase::class.java, "staffDatabase"
            ).build()
        }
    }
    return INSTANCE
}


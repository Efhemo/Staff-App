package com.efhem.mystaff.ui.home

import android.app.Application
import androidx.lifecycle.*
import com.efhem.mystaff.StorageUtil
import com.efhem.mystaff.database.database
import com.efhem.mystaff.model.Staff
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(app: Application) : ViewModel() {

    private val database = database(app.applicationContext)

    private val storage = StorageUtil(app)

    val staffs: LiveData<List<Staff>> = database.daoProfile.getStaffs()

    val staff: MutableLiveData<Staff> = MutableLiveData()

    fun initProfile() {

        viewModelScope.launch {
            staff.value = profile(storage.email)
        }
    }

    fun update(fullname: String, pob: String, specialty: String, dob: String, email: String) {

        viewModelScope.launch {
            updateDb(fullname,pob, specialty, dob, email)
        }
    }

    suspend fun updateDb(fullname: String, pob: String, specialty: String, dob: String, email: String){
        val staff = database.daoProfile.getProfileByEmail(email)
        staff?.let {

            database.daoProfile.insertProfile(Staff(fullname, email, specialty, it.password, dob, pob))
        }
    }

    suspend fun profile(email: String) = withContext(Dispatchers.IO){
        val staff = database.daoProfile.getProfileByEmail(email)
        staff
    }


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
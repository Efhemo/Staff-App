package com.efhem.mystaff.ui.welcome

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.efhem.mystaff.StorageUtil
import com.efhem.mystaff.database.database
import com.efhem.mystaff.model.DBResponse
import com.efhem.mystaff.model.Staff
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WelcomeViewModel(application: Application) : ViewModel() {

    private val database = database(application.applicationContext)
    private val storage = StorageUtil(application)

    val stateMsg = MutableLiveData<DBResponse>()
    val stateMsgSignIn = MutableLiveData<DBResponse>()

    fun signUp(fullname: String, pob: String, dob: String, email: String, password: String) {

        viewModelScope.launch {
            stateMsg.value = saveIn(fullname, pob, dob, email, password)
        }
    }

    fun signIn(email: String, password: String) {

        viewModelScope.launch {
            stateMsgSignIn.value = checkIn(email, password)
        }
    }

    private suspend fun saveIn(
        fullname: String,
        pob: String,
        dob: String,
        email: String,
        password: String
    ): DBResponse = withContext(Dispatchers.IO) {

        //First check if the user already exist
        val staff = database.daoProfile.getProfileByEmail(email)

        if (staff != null) {
            DBResponse(400, "User Already exit")
        } else {
            val long =
                database.daoProfile.insertProfile(Staff(fullname, email, dob = dob, pob = pob, password = password))
            DBResponse(200, "Successful Sign Up")
        }
    }

    private suspend fun checkIn(
        email: String,
        password: String
    ): DBResponse = withContext(Dispatchers.IO) {

        //First check if the user already exist
        val staff = database.daoProfile.getProfileByEmail(email)

        if (staff == null) {
            DBResponse(400, "User with email does Not Exist")
        } else if(staff.password == password){
            DBResponse(404, "Incorrect password")
        }else {

            storage.email = staff.email
            storage.name = staff.fullname

            DBResponse(200, "Successful Sign in")
        }

    }


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WelcomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return WelcomeViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
package com.efhem.mystaff.ui.home


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.efhem.mystaff.R
import com.efhem.mystaff.StorageUtil
import com.efhem.mystaff.database.StaffDatabase
import com.efhem.mystaff.database.database
import com.efhem.mystaff.databinding.FragmentProfileBinding
import com.efhem.mystaff.model.Staff
import com.efhem.mystaff.utils.Constant
import com.efhem.mystaff.utils.getFilePath
import kotlinx.android.synthetic.main.staff_item.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    lateinit var bind: FragmentProfileBinding
    lateinit var database: StaffDatabase
    lateinit var storage: StorageUtil

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        private const val STORAGE_REQUEST_CODE = 2
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this.activity!!, MainViewModel.Factory(requireActivity().application))
            .get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        database = database(this.requireActivity().applicationContext)
        storage = StorageUtil(this.requireContext())
        requestStoragePermission()

        bind.editIcon.setOnClickListener {
            pickPicture(PICK_IMAGE_REQUEST)
        }

        bind.layoutLocation.setOnClickListener {
            bind.expandableLayout.toggle()
        }


        viewModel.initProfile()
        viewModel.staff.observe(this, Observer {
            it?.let {
                val fullname: String = it.fullname
                val email: String = storage.email
                val speciallty: String = if (it.speciallty.isEmpty()) "" else it.speciallty
                val dob: String = it.dob
                val pob: String = it.pob
                val imageUrk: String = if (it.imageUrk.isEmpty()) "" else it.imageUrk

                Glide.with(this.requireContext()).load(imageUrk)
                    .apply(RequestOptions.placeholderOf(R.drawable.female_profile_2))
                    .apply(RequestOptions.errorOf(R.drawable.female_profile_2))
                    .apply(RequestOptions.centerCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(bind.image)

                bind.acctName.text = fullname
                bind.email.text = email
                bind.wrapAddress.editText?.setText(pob)
                bind.specialty.setText(speciallty)
                bind.wrapDob.editText?.setText(dob)
            }
        })

        bind.viewAll.setOnClickListener {
            viewModel.update(
                bind.acctName.text.toString(),
                bind.wrapAddress.editText?.text.toString(),
                bind.wrapSpecialty.editText?.text.toString(),
                bind.wrapDob.editText?.text.toString(),
                bind.email.text.toString()
            )
        }

        return bind.root
    }

    //this methods makes the storage permission request
    private fun requestStoragePermission() {
        //request the permission.
        ActivityCompat.requestPermissions(
            this.requireActivity(),
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_REQUEST_CODE
        )
    }


    private fun showImage(imageUri: Uri) {
        Glide.with(this.requireContext()).load(imageUri)
            .apply(RequestOptions.placeholderOf(R.drawable.female_profile_2))
            .apply(RequestOptions.errorOf(R.drawable.female_profile_2))
            .apply(RequestOptions.centerCropTransform())
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(bind.image)
    }


    private fun pickPicture(request_code: Int) {
        val pickPictureIntent = Intent()
        pickPictureIntent.type = "image/*"

        //pickPictureIntent.setAction(Intent.ACTION_GET_CONTENT);
        pickPictureIntent.action = Intent.ACTION_PICK
        startActivityForResult(pickPictureIntent, request_code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {

            /*val filePathString = getFilePath(data, this.requireContext())
            filePathString?.let {
                //profileVM.updatePic(it)
            }*/

            showImage(data.data!!)
            val filePath = getFilePath(data.data!!, this.activity!!)
            GlobalScope.launch {
                uploadUserImage(File(filePath))
            }
        }
    }


    private fun uploadUserImage(imageFile: File) {
        val map = HashMap<String, String>()
        map[Constant.CLOUD_NAME] = Constant.NAME
        map[Constant.API_KEY] = Constant.KEY
        map[Constant.API_SECRET] = Constant.SECRET

        val cloudinary = Cloudinary(map)
        try {
            Handler(Looper.getMainLooper()).post {
                bind.progressBar.visibility = View.VISIBLE
            }
            val uploadRes = cloudinary.uploader().upload(imageFile, ObjectUtils.emptyMap())
            val imageUrl = uploadRes["secure_url"].toString()

            editProfile(imageUrl, "")
            Log.i("Profile Fragment", "map object is $imageUrl")

            Handler(Looper.getMainLooper()).post {
                bind.progressBar.visibility = View.GONE
                Toast.makeText(this.requireContext(), "Profile pics Updated", Toast.LENGTH_LONG)
                    .show()
            }

        } catch (e: IOException) {
            Handler(Looper.getMainLooper()).post {
                bind.progressBar.visibility = View.GONE
            }
        }


    }

    fun editProfile(imaageUrl: String, specialty: String) {

        val staff = database.daoProfile.getProfileByEmail(storage.email)
        staff?.let {
            val fullname: String = staff.fullname
            val email: String = storage.email
            val speciallty: String = if (staff.speciallty.isEmpty()) specialty else staff.speciallty
            val password: String = staff.password
            val dob: String = staff.dob
            val pob: String = staff.pob
            val imageUrk: String = imaageUrl
            database.daoProfile.insertProfile(
                Staff(
                    fullname,
                    email,
                    speciallty,
                    password,
                    dob,
                    pob,
                    imageUrk
                )
            )
        }
    }

}

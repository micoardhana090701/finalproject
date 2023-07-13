package com.packagesayur.yursayur.activities

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.packagesayur.yursayur.R
import com.packagesayur.yursayur.ViewModelFactory
import com.packagesayur.yursayur.databinding.ActivityAddStoreBinding
import com.packagesayur.yursayur.etc.createCustomTempFile
import com.packagesayur.yursayur.etc.fixImageRotation
import com.packagesayur.yursayur.etc.reduceFileImage
import com.packagesayur.yursayur.etc.uriToFile
import com.packagesayur.yursayur.user.UserPreferences
import com.packagesayur.yursayur.viewmodel.AddStoreViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoreActivity : AppCompatActivity() {
    private var _binding: ActivityAddStoreBinding? = null
    private val binding get() = _binding!!
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private lateinit var currentPhotoPath: String
    private lateinit var addStoreViewModel: AddStoreViewModel
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackAddToko.setOnClickListener {
            finish()
        }
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                AddStoreActivity.REQUIRED_PERMISSIONS,
                AddStoreActivity.REQUEST_CODE_PERMISSION
            )
        }

        val pref = UserPreferences.getInstance(dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(application)
        addStoreViewModel = ViewModelProvider(this, viewModelFactory)[AddStoreViewModel::class.java]
//        CoroutineScope(Dispatchers.IO).launch{
//            getStore()
//        }

        binding.btnEditProfileToko.setOnClickListener {
            showProfileTokoPopup()
        }
        binding.btnEditBannerToko.setOnClickListener {
            showBannerPopup()
        }
        binding.btnAddToko.setOnClickListener {
            if (binding.etNamaToko.text.isEmpty()){
                Toast.makeText(this, "Nama toko tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            if (binding.etLokasiToko.text.isEmpty()){
                Toast.makeText(this, "Lokasi Toko tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else{
                CoroutineScope(Dispatchers.IO).launch {
                    addStore()
                }
                Toast.makeText(this, "Toko Berhasil Dibuat", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

//    private suspend fun getStore(){
//        val pref = UserPreferences.getInstance(dataStore)
//        val viewModelFactory = ViewModelFactory(pref)
//        viewModelFactory.setApplication(application)
//        addStoreViewModel = ViewModelProvider(this, viewModelFactory)[AddStoreViewModel::class.java]
//        val accessToken = pref.getUserKey().first()
//        val userId = intent.getIntExtra(AddStoreActivity.EXTRA_USER_ID, 0)
//        addStoreViewModel.getUserStore(accessToken = "Bearer $accessToken", userId = userId)
//        addStoreViewModel.getUserStore.observe(this){
//            if (it != null){
//                binding.etNamaToko.setText(it.result?.store?.name)
//                binding.etLokasiToko.setText(it.result?.store?.address)
//                val ivFotoProfile = binding.ivProfileToko
//                val ivBannerToko = binding.ivBannerToko
//                if (it.result?.store?.logo == null){
//                    Glide.with(this)
//                        .load(R.drawable.outline_account_circle_24)
//                        .centerCrop()
//                        .into(ivFotoProfile)
//                }
//                else{
//                    Glide.with(this)
//                        .load("https://yursayur.projects.my.id/storage/" + it.result.store.logo)
//                        .centerCrop()
//                        .into(ivFotoProfile)
//                }
//                if (it.result?.store?.banner == null){
//                    Glide.with(this)
//                        .load(R.drawable.outline_account_circle_24)
//                        .centerCrop()
//                        .into(ivBannerToko)
//                }
//                else{
//                    Glide.with(this)
//                        .load("https://yursayur.projects.my.id/storage/" + it.result.store.banner)
//                        .centerCrop()
//                        .into(ivBannerToko)
//                }
//            }
//        }
//    }
    private suspend fun addStore(){
        val nullName = binding.etNamaToko.text.toString()
        val nullCity = binding.etLokasiToko.text.toString()
        val nullDescription = binding.etDeskripsiToko.text.toString()
        if (getFile != null){
            val file = reduceFileImage(getFile as File)
            val name = nullName.toRequestBody("text/plain".toMediaType())
            val city = nullCity.toRequestBody("text/plain".toMediaType())
            val description = nullDescription.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageLogo : MultipartBody.Part = MultipartBody.Part.createFormData(
                "logo",
                file.name,
                requestImageFile
            )
            val imageBanner : MultipartBody.Part = MultipartBody.Part.createFormData(
                "banner",
                file.name,
                requestImageFile
            )
            CoroutineScope(Dispatchers.IO).launch {
                addStoreViewModel.addStore(logo = imageLogo, banner = imageBanner, storeName = name, address = city, description = description)
            }

        }
    }
    private fun showBannerPopup(){
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Pilih Direktori")
        alertDialogBuilder.setIcon(R.drawable.outline_camera_alt_24)

        alertDialogBuilder.setPositiveButton("Camera") { dialogInterface: DialogInterface, i: Int ->
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.resolveActivity(packageManager)
            createCustomTempFile(application).also {
                val photoURI : Uri = FileProvider.getUriForFile(
                    this.applicationContext,
                    "com.packagesayur.yursayur.files",
                    it
                )
                currentPhotoPath = it.absolutePath
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                inCameraBanner.launch(intent)
            }
            dialogInterface.dismiss()
        }

        alertDialogBuilder.setNegativeButton("Galeri") { dialogInterface: DialogInterface, i: Int ->
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            val chooser = Intent.createChooser(intent, "Pilih Gambar")
            inGaleryBanner.launch(chooser)
            dialogInterface.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private val inCameraBanner = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode == RESULT_OK){
            val myFile = File(currentPhotoPath)
            val options = BitmapFactory.Options()
            val bitmap = BitmapFactory.decodeFile(currentPhotoPath, options)
            myFile.let{ file ->
                val fix = fixImageRotation(bitmap, file.path)
                getFile = file
                binding.ivBannerToko.setImageBitmap(fix)
            }
        }
    }

    private val inGaleryBanner = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result ->
        if (result.resultCode == RESULT_OK){
            val setImage: Uri = result.data?.data as Uri
            val myFile = uriToFile(setImage, this)
            binding.ivBannerToko.setImageURI(setImage)
            getFile = myFile
        }
    }

    private fun showProfileTokoPopup(){
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Pilih Direktori")
        alertDialogBuilder.setIcon(R.drawable.outline_camera_alt_24)

        alertDialogBuilder.setPositiveButton("Camera") { dialogInterface: DialogInterface, i: Int ->
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.resolveActivity(packageManager)
            createCustomTempFile(application).also {
                val photoURI : Uri = FileProvider.getUriForFile(
                    this.applicationContext,
                    "com.packagesayur.yursayur.files",
                    it
                )
                currentPhotoPath = it.absolutePath
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                inCameraProfile.launch(intent)
            }
            dialogInterface.dismiss()
        }

        alertDialogBuilder.setNegativeButton("Galeri") { dialogInterface: DialogInterface, i: Int ->
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            val chooser = Intent.createChooser(intent, "Pilih Gambar")
            inGaleryProfile.launch(chooser)
            dialogInterface.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private val inCameraProfile = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode == RESULT_OK){
            val myFile = File(currentPhotoPath)
            val options = BitmapFactory.Options()
            val bitmap = BitmapFactory.decodeFile(currentPhotoPath, options)
            myFile.let{ file ->
                val fix = fixImageRotation(bitmap, file.path)
                getFile = file
                binding.ivProfileToko.setImageBitmap(fix)
            }
        }
    }

    private val inGaleryProfile = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result ->
        if (result.resultCode == RESULT_OK){
            val setImage: Uri = result.data?.data as Uri
            val myFile = uriToFile(setImage, this)
            binding.ivProfileToko.setImageURI(setImage)
            getFile = myFile
        }
    }


    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }
    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSION = 10
        const val EXTRA_USER_ID = "extra_user_id"
    }
}
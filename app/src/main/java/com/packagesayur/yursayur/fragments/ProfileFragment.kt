package com.packagesayur.yursayur.fragments

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.FileProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.packagesayur.yursayur.R
import com.packagesayur.yursayur.ViewModelFactory
import com.packagesayur.yursayur.activities.AddStoreActivity
import com.packagesayur.yursayur.activities.LoginActivity
import com.packagesayur.yursayur.activities.UserUpdateActivity
import com.packagesayur.yursayur.databinding.FragmentProfileBinding
import com.packagesayur.yursayur.etc.Resource
import com.packagesayur.yursayur.etc.createCustomTempFile
import com.packagesayur.yursayur.etc.fixImageRotation
import com.packagesayur.yursayur.etc.reduceFileImage
import com.packagesayur.yursayur.etc.uriToFile
import com.packagesayur.yursayur.user.AuthViewModel
import com.packagesayur.yursayur.user.UserPreferences
import com.packagesayur.yursayur.viewmodel.ProfileViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ProfileFragment : Fragment() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private var _binding: FragmentProfileBinding? = null
    private lateinit var currentPhotoPath: String
    private val binding get() =  _binding!!
    private lateinit var authViewModel: AuthViewModel
    private lateinit var profileViewModel: ProfileViewModel
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            setupProfileView(view)
        }

        binding.btnLogOut.setOnClickListener {
            authViewModel.logout()
            startActivity(Intent(context, LoginActivity::class.java))
            finishAffinity(requireActivity())
        }

        binding.btnUbahFoto.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setTitle("Pilih Direktori")
            alertDialogBuilder.setIcon(R.drawable.outline_camera_alt_24)

            alertDialogBuilder.setPositiveButton("Camera") { dialogInterface: DialogInterface, i: Int ->
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.resolveActivity(requireActivity().packageManager)
                createCustomTempFile(requireContext().applicationContext).also {
                    val photoURI : Uri = FileProvider.getUriForFile(
                        requireContext().applicationContext,
                        "com.packagesayur.yursayur.files",
                        it
                    )
                    currentPhotoPath = it.absolutePath
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    inCamera.launch(intent)
                }
                dialogInterface.dismiss()
            }

            alertDialogBuilder.setNegativeButton("Galeri") { dialogInterface: DialogInterface, i: Int ->
                val intent = Intent()
                intent.action = Intent.ACTION_GET_CONTENT
                intent.type = "image/*"
                val chooser = Intent.createChooser(intent, "Pilih Gambar")
                inGalery.launch(chooser)
                dialogInterface.dismiss()
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        binding.btnBuatToko.setOnClickListener {
            startActivity(Intent(context, AddStoreActivity::class.java))
        }

        binding.btnUbahDataProfile.setOnClickListener {
            startActivity(Intent(context, UserUpdateActivity::class.java))
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root

    }

    private suspend fun setupProfileView(view: View){
        val pref = UserPreferences.getInstance(requireContext().dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(requireActivity().application)

        profileViewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
        authViewModel = ViewModelProvider(this, ViewModelFactory(pref))[AuthViewModel::class.java]

        val accessToken = pref.getUserKey().first()
        profileViewModel.getProfile(access_token = "Bearer $accessToken")
        profileViewModel.profileUser.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    if (it != null){
                        val userIdIntent = Intent(context, AddStoreActivity::class.java)
                        userIdIntent.putExtra(AddStoreActivity.EXTRA_USER_ID, it.data?.result?.user?.id)
                        binding.tvNamaUser.setText(it.data?.result?.user?.name)
                        binding.tvAlamatProfile.setText(it.data?.result?.user?.address)
                        val ivProfileHome = binding.ivProfileUser
                        if (it.data?.result?.user?.avatar == null){
                            Glide.with(view.context)
                                .load(R.drawable.outline_account_circle_24)
                                .centerCrop()
                                .into(ivProfileHome)
                        }
                        else{
                            Glide.with(view.context)
                                .load("https://yursayur.projects.my.id/storage/" + it.data.result.user.avatar)
                                .centerCrop()
                                .into(ivProfileHome)
                        }
                    }
                    else{
                        val ivProfileHome = binding.ivProfileUser
                        binding.tvNamaUser.setText(it?.data?.result?.user?.name)
                        Glide.with(view.context)
                            .load(R.drawable.outline_account_circle_24)
                            .centerCrop()
                            .into(ivProfileHome)
                    }
                }
                is Resource.Loading -> {
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                else -> {
                }
            }
        }
    }

    private suspend fun update() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())

            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "avatar",
                file.name,
                requestImageFile
            )
            profileViewModel.profileUser.observe(this){
                val nama = it.data?.result?.user?.name.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val email = it.data?.result?.user?.email.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val address = it.data?.result?.user?.address.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val phone = it.data?.result?.user?.phone.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                lifecycleScope.launch{
                    profileViewModel.updateAll(image = imageMultipart, email = email, name = nama, address = address, phone = phone)
                }
                if (it != null) {
                    Toast.makeText(requireContext(), "Akun Telah di Update", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val inCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == AppCompatActivity.RESULT_OK){
            val myFile = File(currentPhotoPath)
            val options = BitmapFactory.Options()
            val bitmap = BitmapFactory.decodeFile(currentPhotoPath, options)
            myFile.let { file ->
                val fix = fixImageRotation(bitmap, file.path)
                getFile = file
                binding.ivProfileUser.setImageBitmap(fix)
                lifecycleScope.launch {
                    update()
                }
            }
        }
    }

    private val inGalery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK){
            val setImage: Uri = result.data?.data as Uri
            val myFile = uriToFile(setImage, requireActivity())
            binding.ivProfileUser.setImageURI(setImage)
            getFile = myFile
            lifecycleScope.launch {
                update()
            }
        }
    }
}
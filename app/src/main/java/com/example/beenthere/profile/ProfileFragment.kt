package com.example.beenthere.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import com.example.beenthere.data.User
import com.example.beenthere.databinding.FragmentProfileBinding
import com.example.beenthere.utils.UserManager.tempAvatar
import com.example.beenthere.utils.UserManager.userAvatar
import com.example.beenthere.utils.UserManager.userName
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class ProfileFragment : Fragment() {

    private val db = Firebase.firestore
    private val userDocRef = db.collection("users").document()

    private var userRole = 0
    private lateinit var binding: FragmentProfileBinding
    private val PICK_IMAGE_REQUEST = 1
    private val storageRef = FirebaseStorage.getInstance().getReference("profile_photos")

    //    private val fileName = "$userId.jpg"
    private val fileName = "Robb.jpg"
    private val imageRef = storageRef.child(fileName)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        binding = FragmentProfileBinding.inflate(inflater, container, false)

//        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

//        requestPermission()
//
//        binding.btnJoinChannel.setOnClickListener {
//            onSubmit()
//        }

        binding.avatarFrame.setImageURI(userAvatar.toUri())

        binding.editPhoto.setOnClickListener {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
        }

        userName = "Roland"
//        binding.avatarFrame.setImageURI(tempAvatar.toUri())


        return binding.root
    }

//    @RequiresApi(Build.VERSION_CODES.S)
//    private fun requestPermission() {
//        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.BLUETOOTH_CONNECT), 22)
//    }

//    private fun onSubmit(
////        view: View
//    ) {
//        val channelName = "rol"
//        val userRadioButton = binding.radioGroup
//
//        val checked = userRadioButton.checkedRadioButtonId
//        val audienceButtonId = binding.radioAudience
//
//        userRole = if (checked == audienceButtonId.id) {
//            0
//        } else {
//            1
//        }
//
//        val intent = Intent(requireActivity(), VideoActivity::class.java)
//        intent.putExtra("ChannelName", channelName)
//        intent.putExtra("UserRole", userRole)
//        startActivity(intent)
//    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val selectedImageUri = data.data
            val uploadTask = selectedImageUri?.let { imageRef.putFile(it) }
            uploadTask
                ?.addOnSuccessListener {
                    imageRef.downloadUrl
                        .addOnSuccessListener { uri ->
                            val downloadUrl = uri.toString()
                            val user = User(
                                userId = userDocRef.id,
                                userName = userName,
                                userAvatar = downloadUrl
                            )
                            userDocRef.set(user)
//                            userDocRef.set("profilePhotoUrl", downloadUrl)
                                .addOnSuccessListener {
                                    Log.i("Profile frag update", "Success $downloadUrl")
                                }
                                .addOnFailureListener { exception ->
                                    Log.i("Profile frag update", "failure $exception")
                                }
                        }
                        .addOnFailureListener { exception2 ->
                            Log.i("Profile frag download", "failure $exception2")
                        }
                }
                ?.addOnFailureListener { exception3 ->
                    Log.i("Profile frag upload", "failure $exception3")
                }

            Log.i("Profile frag ImageURI", selectedImageUri.toString())

            userAvatar = selectedImageUri.toString()
            binding.avatarFrame.setImageURI(selectedImageUri)
        }
    }


}
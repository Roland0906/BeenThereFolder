package com.example.beenthere.profile

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import com.example.beenthere.R
import com.example.beenthere.data.User
import com.example.beenthere.databinding.FragmentProfileBinding
import com.example.beenthere.ext.getVmFactory
import com.example.beenthere.utils.UserManager.userAvatar
import com.example.beenthere.utils.UserManager.userID
import com.example.beenthere.utils.UserManager.userName
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class ProfileFragment : Fragment() {

    private val db = Firebase.firestore
    private val userDocRef = db.collection("users").document()

    private lateinit var binding: FragmentProfileBinding
    private val PICK_IMAGE_REQUEST = 1
    private val storageRef = FirebaseStorage.getInstance().getReference("profile_photos")

    //    private val fileName = "$userId.jpg"
    private val fileName = "Robb.jpg"
    private val imageRef = storageRef.child(fileName)

    // log in
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private val viewModel by viewModels<ProfileViewModel> { getVmFactory() }

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

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        binding.avatarFrame.setImageURI(userAvatar.toUri())

        binding.avatarFrame.setOnClickListener {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
        }

//        binding.avatarFrame.setImageURI(tempAvatar.toUri())


        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(requireContext().getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        binding.btnLogIn.setOnClickListener {
            googleSignIn()
        }

        binding.btnLogOut.setOnClickListener {
            googleLogOut()
        }


        checkLogInStatus()


        viewModel.getUser(getUserInfo())


        return binding.root
    }

    private fun getUserInfo(): User {
        val sharedPreferences =
            requireContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE)

        return User(
            sharedPreferences.getString("userId", null).toString(),
            sharedPreferences.getString("userName", null).toString(),
            sharedPreferences.getString("userAvatar", null).toString()
        )
    }

    private fun checkLogInStatus() {
        if (auth.currentUser != null) {
            binding.btnLogOut.visibility = View.VISIBLE
            binding.btnLogIn.visibility = View.GONE
        } else {
            binding.btnLogOut.visibility = View.GONE
            binding.btnLogIn.visibility = View.VISIBLE
        }
    }

    private fun googleLogOut() {
        // log out firebase
        auth.signOut()

        // log out google sign in client
        googleSignInClient.signOut().addOnCompleteListener {
            Toast.makeText(this.context, "Logged out", Toast.LENGTH_SHORT).show()
        }
        viewModel.getUserName("")
        viewModel.getUserAvatar("")
        clearUserId()


    }

    private fun googleSignIn() {
        val signInClient = googleSignInClient.signInIntent
        launcher.launch(signInClient)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

                result ->

            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                manageResults(task)
            }
        }

    private fun manageResults(task: Task<GoogleSignInAccount>) {
        val account: GoogleSignInAccount? = task.result

        if (account != null) {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential).addOnCompleteListener {
                if (task.isSuccessful) {

                    val firebaseUser = auth.currentUser
                    val uid = firebaseUser?.uid


                    if (uid != null) {

                        try {
                            // to sharedPref
                            storeUserInfo(
                                uid,
                                account.displayName.toString(),
                                account.photoUrl.toString()
                            )

                            // to UserManager
                            userID = uid
                            userName = account.displayName.toString()
                            userAvatar = account.photoUrl.toString()
                            viewModel.getUserName(account.displayName.toString())
                            viewModel.getUserAvatar(account.photoUrl.toString())

                            Log.i("Profile", "Log in info ${account.displayName} + ${account.photoUrl}")

                        } catch (e: Exception) {
                            Log.i("Profile", "try catch fail")
                        }

                    }

                    Toast.makeText(this.context, "Log in successfully", Toast.LENGTH_SHORT).show()

                    checkLogInStatus()

                    // load user info

                } else {
                    Toast.makeText(this.context, task.exception.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } else {
            Toast.makeText(this.context, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }


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
//                                userName = userName,
//                                userAvatar = downloadUrl
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

    private fun storeUserInfo(uid: String, name: String, avatar: String) {
        val sharedPreferences = requireContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userId", uid)
        editor.putString("userName", name)
        editor.putString("userAvatar", avatar)
        editor.apply()
    }


    // Clear the user ID when the user logs out
    private fun clearUserId() {
        val sharedPreferences = requireContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("userId")
        editor.remove("userName")
        editor.remove("userAvatar")
        editor.apply()
    }


}
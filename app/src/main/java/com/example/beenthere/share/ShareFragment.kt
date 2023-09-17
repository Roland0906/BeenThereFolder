package com.example.beenthere.share

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.example.beenthere.BuildConfig
import com.example.beenthere.R
import com.example.beenthere.databinding.FragmentHomeBinding
import com.example.beenthere.databinding.FragmentShareBinding
import com.example.beenthere.ext.getVmFactory
import com.example.beenthere.utils.Constants
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Locale


class ShareFragment : Fragment() {

    private lateinit var binding: FragmentShareBinding

    val db = Firebase.firestore

    private val viewModel by viewModels<ShareViewModel> { getVmFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        binding = FragmentShareBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.btnSearch.setOnClickListener {
            if (checkInternet(requireContext())) {
                val title = binding.editInputBook.text.toString()
                if (title.isNotEmpty()) {

                    viewModel.getBooks(title, BuildConfig.BOOK_API_KEY)
                    viewModel.myResponse.observe(viewLifecycleOwner) { response ->
                        if (response.isSuccessful) {

                            val item = response.body()!!.items[0]

                            binding.bookTitleResult.text = item.volumeInfo?.title
                            binding.authorNameResult.text = item.volumeInfo?.authors?.get(0) ?: ""

                            Log.i("Book test", item.volumeInfo?.imageLinks?.smallThumbnail.toString())

                            try {
                                viewModel.getImage(item.volumeInfo?.imageLinks?.smallThumbnail.toString())
                            } catch (e: Exception) {
                                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                            }

                        } else {

                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Title can not be empty", Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                Toast.makeText(requireContext(), "Please connect to internet", Toast.LENGTH_SHORT)
                    .show()
            }
        }


        // fake user id
        val userList = listOf("PollyYana", "Jammy", "Jayson", "Terry", "Elephant", "Timothy", "Bryant", "Jackson")

        var userId = ""
        var title: String = ""
        var author: String = ""
        var situation: String = ""
        var phrases: String = ""


        binding.bookTitleResult.doAfterTextChanged {
            title = binding.bookTitleResult.text.toString()
        }

        binding.authorNameResult.doAfterTextChanged {
            author = binding.authorNameResult.text.toString()
        }

        binding.editSituation.doAfterTextChanged {
            situation = binding.editSituation.text.toString()
        }

        binding.editPhrases.doAfterTextChanged {
            phrases = binding.editPhrases.text.toString()
        }


        binding.btnSend.setOnClickListener {
            userId = userList.random()
            if (title == "" && author == "" && situation == "" && phrases == "") {
                Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show()
            } else {
                viewModel.addData(userId, title, author, situation, phrases)
                binding.editInputBook.setText("")
                viewModel.getImage("")
                binding.bookTitleResult.text = ""
                binding.authorNameResult.text = ""
                binding.editSituation.setText("")
                binding.editPhrases.setText("")
            }
        }


        viewModel.toastMessageLiveData.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }





        return binding.root
    }

    private fun checkInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork =
                connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }


}
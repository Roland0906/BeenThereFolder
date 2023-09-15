package com.example.beenthere.home

import android.content.Context
import android.content.Intent
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.beenthere.MainActivity
import com.example.beenthere.R
import com.example.beenthere.adapter.BookSearchResultAdapter
import com.example.beenthere.databinding.FragmentHomeBinding
import com.example.beenthere.ext.getVmFactory
import com.example.beenthere.utils.Constants
import com.gdsc.bbsbec.gbooks.model.BookSearchResultData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.util.Locale


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    val db = Firebase.firestore

//    private val viewModel: HomeViewModel by lazy {
//        ViewModelProvider(this)[HomeViewModel::class.java]
//    }

    private val viewModel by viewModels<HomeViewModel> { getVmFactory() }

    private var pass: Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val id = ArrayList<String>()
        val bookName = ArrayList<String>()
        val bookPublisher = ArrayList<String>()
        val bookSmallThumbnail = ArrayList<String>()

        val bookThumbnail = ArrayList<String>()
        val bookDescription = ArrayList<String>()
        val previewLink = ArrayList<String>()


        val commonQueryParams = mapOf(
            "fields" to "items(volumeInfo/description,volumeInfo/title,volumeInfo/imageLinks/smallThumbnail)",
            "key" to Constants.API_KEY
        )

        binding.btnSearch.setOnClickListener {

//            viewModel.searchBook(binding.editInputBook.text.toString())
//            searchBook()
            if (checkInternet(requireContext())) {
                val title = binding.editInputBook.text.toString()
                if (title.isNotEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Searching books having \"${
                            title.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                            }
                        }\" in name.",
                        Toast.LENGTH_LONG
                    ).show()

//                    viewModel.getBooks(title, commonQueryParams)
                    viewModel.getBooks(title, Constants.API_KEY)
                    viewModel.myResponse.observe(viewLifecycleOwner) { response ->
                        if (response.isSuccessful) {



//                            response.body()!!.items.forEach {
//                                id.add(it.id.toString())
//                                bookName.add(it.volumeInfo!!.title.toString())
//                                bookPublisher.add(it.volumeInfo!!.publisher.toString())
//                                bookSmallThumbnail.add(it.volumeInfo!!.imageLinks!!.smallThumbnail.toString())
//                                bookThumbnail.add(it.volumeInfo!!.imageLinks!!.thumbnail.toString())
//                                bookDescription.add(it.volumeInfo!!.description.toString())
//                                previewLink.add(it.volumeInfo!!.previewLink.toString())
//                            }



                            val item = response.body()!!.items[0]
//                            Log.i("HomeFrag", item.toString())
//                            Log.i("Book test", response.body()!!.items.toString())
                            binding.bookTitleResult.text = item.volumeInfo?.title
                            binding.authorNameResult.text = item.volumeInfo?.authors?.get(0) ?: ""
//                            binding.bookImageResult.load(item.volumeInfo?.imageLinks?.thumbnail)

                            Log.i("Book test", item.volumeInfo?.imageLinks?.smallThumbnail.toString())
//                            binding.bookImageResult.load(item.volumeInfo?.imageLinks?.smallThumbnail.toString())
                            try {
                                viewModel.getImage(item.volumeInfo?.imageLinks?.smallThumbnail.toString())
                            } catch (e: Exception) {
                                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                            }

//                            item.volumeInfo?.imageLinks?.smallThumbnail?.let { it1 ->
//                                viewModel.getImage(
//                                    it1
//                                )
//                            }
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

        val data = arrayListOf<BookSearchResultData>()
        repeat(bookName.size - 1) {
            data.add(
                BookSearchResultData(
                    id[pass],
                    bookSmallThumbnail[pass],
                    bookName[pass],
                    bookPublisher[pass],
                    bookDescription[pass],
                    previewLink[pass],
                    bookThumbnail[pass],
                )
            )
            pass += 1
        }

        val bookSearchResultAdapter = BookSearchResultAdapter(data)
        binding.bookSearchResultRecyclerview.adapter = bookSearchResultAdapter





        viewModel.toastMessageLiveData.observe(viewLifecycleOwner, Observer
        { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })

        viewModel.bookTitle.observe(viewLifecycleOwner, Observer
        {
            binding.bookTitleResult.text = it
        })

        viewModel.bookAuthor.observe(viewLifecycleOwner, Observer
        {
            binding.authorNameResult.text = it
        })

        viewModel.bookImage.observe(viewLifecycleOwner, Observer {
            binding.bookImageResult.load(it)
        })


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
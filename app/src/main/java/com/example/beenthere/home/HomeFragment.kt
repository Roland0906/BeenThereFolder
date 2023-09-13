package com.example.beenthere.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.beenthere.MainActivity
import com.example.beenthere.R
import com.example.beenthere.databinding.FragmentHomeBinding
import com.example.beenthere.ext.getVmFactory
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    val db = Firebase.firestore

//    private val viewModel: HomeViewModel by lazy {
//        ViewModelProvider(this)[HomeViewModel::class.java]
//    }

    private val viewModel by viewModels<HomeViewModel> { getVmFactory() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel

        binding.btnSearch.setOnClickListener {
            searchBook()
        }








        return binding.root
    }

    private fun searchBook() {
        val query = binding.editInputBook.text.toString()
        val client = AsyncHttpClient()
        val url = "https://www.googleapis.com/books/v1/volumes?q=${query}"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d(TAG, result)

                try {
                    val jsonObject = JSONObject(result)
                    val itemsArray = jsonObject.getJSONArray("items")

                    var i = 0
                    var bookTitle = ""
                    var bookAuthor = ""

                    while (i < itemsArray.length()) {
                        val book = itemsArray.getJSONObject(i)
                        val volumeInfo = book.getJSONObject("volumeInfo")
                        try {
                            bookTitle = volumeInfo.getString("title")
                            bookAuthor = volumeInfo.getString("authors")
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        i ++
                    }

                    binding.apply {
                        bookTitleResult.text = bookTitle
                        authorNameResult.text = bookAuthor
                    }

                } catch (e: Exception) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
               val errorMessage = when (statusCode) {
                   401 -> "Status code: Bad request"
                   403 -> "Status code: Forbidden"
                   404 -> "Status code: Not found"
                   else -> "Status code: ${error?.message}"
               }
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }


}
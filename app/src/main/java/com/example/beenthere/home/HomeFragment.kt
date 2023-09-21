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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.beenthere.MainActivity
import com.example.beenthere.NavigationDirections
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.Locale


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel by viewModels<HomeViewModel> { getVmFactory() }

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

//        binding.btnSearch.setOnClickListener {
//            val question = binding.messageEditText.text.toString()
//            chatGptViewModel.addToChat(question, Message.SENT_BY_ME, chatGptViewModel.getCurrentTimestamp())
//            binding.messageEditText.setText("")
//            viewModel.callApi(question)
//        }


//        viewModel.allExp.observe(viewLifecycleOwner) {
//            viewModel.analyzer()
//        }

        lifecycleScope.launch {
            viewModel.allExp().collect {
                viewModel.analyzer(it)
            }
        }

        binding.btnToMeaning.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToCategoryFragment(CATEGORY.LIFE_MEANING.name))
        }

        binding.btnToCommunication.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToCategoryFragment(CATEGORY.COMMUNICATION.name))
        }

        binding.btnToDiscipline.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToCategoryFragment(CATEGORY.DISCIPLINE.name))
        }

        binding.btnToLearning.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToCategoryFragment(CATEGORY.LEARNING.name))
        }

        binding.btnToWork.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToCategoryFragment(CATEGORY.WORK.name))
        }

        binding.btnToRelationship.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToCategoryFragment(CATEGORY.RELATIONSHIP.name))
        }



        Log.i("HomeFragment", viewModel.categories.toString())


        return binding.root
    }

    override fun onStop() {
        super.onStop()
        viewModel.removeFirebaseListener()
    }
}
package com.example.beenthere.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.beenthere.R
import com.example.beenthere.databinding.FragmentHomeBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    val db = Firebase.firestore

    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]
    }
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










        return binding.root
    }


}
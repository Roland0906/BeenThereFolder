package com.example.beenthere.home.catogories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.beenthere.R
import com.example.beenthere.databinding.FragmentHomeBinding
import com.example.beenthere.databinding.FragmentMeaningBinding
import com.example.beenthere.ext.getVmFactory
import com.example.beenthere.home.CATEGORY
import com.example.beenthere.home.HomeViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MeaningFragment : Fragment() {

    private lateinit var binding: FragmentMeaningBinding

    private val viewModel by viewModels<CategoryVM> { getVmFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMeaningBinding.inflate(inflater, container, false)
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

//        val adapter = CategoryAdapter(CategoryAdapter.OnClickListener {
//
//        })

        val arrayList: ArrayList<String> = ArrayList()

        arrayList.add("")

        val adapter = this.context?.let { ImageAdapter(it, arrayList) }



        lifecycleScope.launch {
            viewModel.allExp().map {
                val filteredList = it.filter { exp ->
                    exp.category == CATEGORY.LIFE_MEANING.toString()
                }
                filteredList
            }
                .collect {

                }
        }


        return binding.root
    }


}
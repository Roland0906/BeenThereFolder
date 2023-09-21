package com.example.beenthere.home.catogories.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.beenthere.R
import com.example.beenthere.databinding.FragmentDetailBinding

import com.example.beenthere.ext.getVmFactory



class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding

    private val viewModel by viewModels<DetailVM> {
        getVmFactory(
            DetailFragmentArgs.fromBundle(
                requireArguments()
            ).experience
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailBinding.inflate(inflater, container, false)
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.btnGoBack.setOnClickListener {
            findNavController().popBackStack()
        }




        return binding.root
    }


}
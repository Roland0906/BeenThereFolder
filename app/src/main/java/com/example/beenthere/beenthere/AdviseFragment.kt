package com.example.beenthere.beenthere

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.beenthere.R
import com.example.beenthere.databinding.FragmentAdviseBinding
import com.example.beenthere.databinding.FragmentDetailBinding
import com.example.beenthere.ext.getVmFactory
import com.example.beenthere.home.catogories.detail.DetailFragmentArgs
import com.example.beenthere.home.catogories.detail.DetailVM


class AdviseFragment : Fragment() {

    private lateinit var binding: FragmentAdviseBinding

    private val viewModel by viewModels<AdviseViewModel> {
        getVmFactory(
            AdviseFragmentArgs.fromBundle(
                requireArguments()
            ).situation
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdviseBinding.inflate(inflater, container, false)

        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.btnGoBack.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }


}
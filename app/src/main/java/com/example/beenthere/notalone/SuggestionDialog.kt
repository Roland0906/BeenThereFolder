package com.example.beenthere.notalone

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.beenthere.R
import com.example.beenthere.beenthere.BeenThereAdapter
import com.example.beenthere.databinding.FragmentNotAloneBinding
import com.example.beenthere.databinding.FragmentSuggestionDialogBinding
import com.example.beenthere.ext.getVmFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch


class SuggestionDialog : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentSuggestionDialogBinding

    private val viewModel by viewModels<SuggestionViewModel> { getVmFactory() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        binding = FragmentSuggestionDialogBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = DialogAdapter()

        binding.recyclerDialog.adapter = adapter

        lifecycleScope.launch {
            viewModel.allExp().collect { experiences ->
                val images = experiences.map {it.image}
                val extractedList = if (images.size >= 3) {
                    images.subList(1, 4) // Extract the first three items
                } else {
                    images // If there are less than three items, keep the original list
                }
                adapter.submitList(extractedList)
            }
        }





        return binding.root
    }


}
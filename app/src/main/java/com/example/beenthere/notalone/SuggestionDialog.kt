package com.example.beenthere.notalone

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.beenthere.R
import com.example.beenthere.beenthere.BeenThereAdapter
import com.example.beenthere.data.Situation
import com.example.beenthere.databinding.FragmentNotAloneBinding
import com.example.beenthere.databinding.FragmentSuggestionDialogBinding
import com.example.beenthere.ext.getVmFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch


class SuggestionDialog : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNotAloneBinding

    private val viewModel by viewModels<NotAloneViewModel> { getVmFactory() }
    private lateinit var typingText: TextView
    private val textToType = "Tell us what happened"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        binding = FragmentNotAloneBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        var description = ""
        binding.inputSituation.doAfterTextChanged {
            description = binding.inputSituation.text.toString()
        }

        val userList = listOf("DongGert", "Elyes", "JsonString", "TimDer")

        var userId = ""

        binding.btnSearch.setOnClickListener {

            if (description == "") {
                Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show()
            } else {
                userId = userList.random()
                val situation = Situation(userId = userId, description = description)
                viewModel.addData(situation)
                binding.inputSituation.setText("")

            }
        }

        binding.inputSituation.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_DOWN) {
                hideKeyboard()

                return@setOnKeyListener true
            }

            false
        }

        typingText = binding.text

        animateTextTyping(0)

//        val adapter = DialogAdapter()
//
//        binding.recyclerDialog.adapter = adapter
//
//        lifecycleScope.launch {
//            viewModel.allExp().collect { experiences ->
//                val images = experiences.map {it.image}
//                val extractedList = if (images.size >= 3) {
//                    images.subList(1, 4) // Extract the first three items
//                } else {
//                    images // If there are less than three items, keep the original list
//                }
//                adapter.submitList(extractedList)
//            }
//        }





        return binding.root
    }

    private fun hideKeyboard() {
        val imm = this.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.inputSituation.windowToken, 0)
    }

    private fun animateTextTyping(index: Int) {
        if (index < textToType.length) {
            val currentText = textToType.substring(0, index + 1)
            typingText.text = currentText
            val delay = 100L
            Handler().postDelayed({
                animateTextTyping(index + 1)
            }, delay)
        }
    }


}
package com.example.beenthere.notalone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.beenthere.NavigationDirections
import com.example.beenthere.R
import com.example.beenthere.data.Situation
import com.example.beenthere.databinding.FragmentNotAloneBinding
import com.example.beenthere.databinding.FragmentShareBinding
import com.example.beenthere.ext.getVmFactory


class NotAloneFragment : Fragment() {

    private lateinit var binding: FragmentNotAloneBinding

    private val viewModel by viewModels<NotAloneViewModel> { getVmFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        binding = FragmentNotAloneBinding.inflate(inflater, container, false)

//        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.environWhite.setOnClickListener {
            binding.description.visibility = View.GONE
            binding.environWhite.visibility = View.GONE
            binding.environGray.visibility = View.GONE
            binding.environDark.visibility = View.GONE

            binding.topFrame.setBackgroundResource(R.drawable.environ_white_big)
        }

        binding.environGray.setOnClickListener {
            binding.description.visibility = View.GONE
            binding.environWhite.visibility = View.GONE
            binding.environGray.visibility = View.GONE
            binding.environDark.visibility = View.GONE

            binding.topFrame.setBackgroundResource(R.drawable.envrion_gray_big)
        }

        binding.environDark.setOnClickListener {
            binding.description.visibility = View.GONE
            binding.environWhite.visibility = View.GONE
            binding.environGray.visibility = View.GONE
            binding.environDark.visibility = View.GONE

            binding.topFrame.setBackgroundResource(R.drawable.envrion_dark_big)
        }

        val userList = listOf("DongGert", "Elyes", "JsonString", "TimDer")

        var userId = ""
        var description = ""

        binding.inputSituation.doAfterTextChanged {
            description = binding.inputSituation.text.toString()
        }



        binding.btnSearch.setOnClickListener {

            if (description == "") {
                Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show()
            } else {
                userId = userList.random()
                val situation = Situation(userId = userId, description = description)
                viewModel.addData(situation)
                binding.inputSituation.setText("")
//                it.findNavController().navigate(NavigationDirections.navigateToSuggestionDialog())
            }
        }

        viewModel.toastMessageLiveData.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }









        return binding.root
    }


}
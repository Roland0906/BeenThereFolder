package com.example.beenthere.notalone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.example.beenthere.R
import com.example.beenthere.databinding.FragmentNotAloneBinding
import com.example.beenthere.databinding.FragmentShareBinding


class NotAloneFragment : Fragment() {

    private lateinit var binding: FragmentNotAloneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        // Inflate the layout for this fragment


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



        return binding.root
    }


}
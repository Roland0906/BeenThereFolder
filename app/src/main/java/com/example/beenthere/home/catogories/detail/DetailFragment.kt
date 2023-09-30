package com.example.beenthere.home.catogories.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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

        viewModel.setFireStoreListener()

//        val adapter = ForumAdapter()
//        binding.recyclerForum.adapter = adapter

        viewModel.messageList.observe(viewLifecycleOwner) {
            Log.i("Detail Frag", it.toString())
            val adapter = ForumAdapter()
            binding.recyclerForum.adapter = adapter
            adapter.submitList(it)
            Log.i("Detail Frag submit", "submitted list")
        }


        val userList = listOf("PollyYana", "Jammy", "Jayson", "Terry", "Elephant", "Timothy", "Bryant", "Jackson", "YenL")


        var comment = ""

        binding.inputComment.doAfterTextChanged {
            comment = binding.inputComment.text.toString()
        }

        binding.btnSend.setOnClickListener {
            viewModel.addComment(
                id = userList.random(),
                message = comment,
            )
            binding.inputComment.setText("")
        }

        binding.scroll.isVerticalFadingEdgeEnabled = true
        binding.recyclerForum.isVerticalFadingEdgeEnabled = true




        return binding.root
    }


}
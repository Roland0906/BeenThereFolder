package com.example.beenthere.beenthere

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.beenthere.R
import com.example.beenthere.databinding.FragmentAdviseBinding
import com.example.beenthere.ext.getVmFactory
import com.example.beenthere.home.catogories.detail.ForumAdapter


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

        binding = FragmentAdviseBinding.inflate(inflater, container, false)

        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.btnGoBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val fakeImages = listOf(
            R.drawable.avatar,
            R.drawable.avatar_2,
            R.drawable.avatar_3_female,
            R.drawable.avatar_4_female,
            R.drawable.avatar_5,
            R.drawable.avatar_6
        )

        binding.avatar.setBackgroundResource(fakeImages.random())


        viewModel.setFireStoreListener()

        viewModel.messageList.observe(viewLifecycleOwner) {
            Log.i("Advise Frag", it.toString())
            val adapter = ForumAdapter()
            binding.recyclerAdvise.adapter = adapter
            adapter.submitList(it)
            Log.i("Advise Frag submit", "submitted list")
        }

        val userList = listOf("PollyYana", "Jammy", "Jayson", "Terry", "Elephant", "Timothy", "Bryant", "Jackson", "YenL")


        var comment = ""

        binding.inputComment.doAfterTextChanged {
            comment = binding.inputComment.text.toString()
        }

        binding.btnSend.setOnClickListener {
            viewModel.addAdvice(
                id = userList.random(),
                message = comment,
            )
            binding.inputComment.setText("")
        }

        binding.recyclerAdvise.isVerticalFadingEdgeEnabled = true




        return binding.root
    }


}
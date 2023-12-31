package com.example.beenthere.home.catogories.detail

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.beenthere.data.User
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

        val userList = listOf("PollyYana", "Jammy", "Jayson", "Terry", "Elephant", "Tom", "Bryant", "Jackson", "YenZero")

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

        binding.inputComment.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.inputComment.hint = ""
            } else {
                binding.inputComment.hint = "Leave a comment"
            }
        }

        val user = getUserInfo()
        viewModel.checkIfExpIsLiked(user.userId)
//        viewModel.setLikedExpListener(user.userId)

        binding.toLove.setOnClickListener {
//            viewModel.clickLike()
            viewModel.saveExp(user.userId)
//            updateButtonBackground()
        }

        binding.loved.setOnClickListener {
            viewModel.deleteExp(user.userId)
        }

        viewModel.isLiked.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.toLove.visibility = View.GONE
                binding.loved.visibility = View.VISIBLE
            } else {
                binding.toLove.visibility = View.VISIBLE
                binding.loved.visibility = View.GONE
            }
        }


//        viewModel.isLiked.observe(viewLifecycleOwner) {
//            viewModel.updateLikeExp(UserManager.userID)
//            binding.toLove.setBackgroundResource(
//                if (it == true) {
//                    R.drawable.icon_loved
//                } else {
//                    R.drawable.icon_unloved
//                }
//            )
//        }


        binding.inputComment.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_DOWN) {
                hideKeyboard()

                return@setOnKeyListener true
            }

            false
        }

        return binding.root
    }

    private fun hideKeyboard() {
        val imm = this.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.inputComment.windowToken, 0)
    }

//    private fun updateButtonBackground() {
//        binding.toLove.setBackgroundResource(
//            if (viewModel.isLiked.value == true) {
//                R.drawable.icon_loved
//            } else {
//                R.drawable.icon_unloved
//            }
//        )
//    }

    private fun getUserInfo(): User {
        val sharedPreferences =
            requireContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE)

        return User(
            sharedPreferences.getString("userId", null).toString(),
            sharedPreferences.getString("userName", null).toString(),
            sharedPreferences.getString("userAvatar", null).toString()
        )
    }


}
package com.example.beenthere.home

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.beenthere.NavigationDirections
import com.example.beenthere.R
import com.example.beenthere.VideoActivity
import com.example.beenthere.databinding.FragmentHomeBinding
import com.example.beenthere.ext.getVmFactory
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {


    private var userRole = 0
    private lateinit var binding: FragmentHomeBinding

    private val viewModel by viewModels<HomeViewModel> { getVmFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

//        binding.btnSearch.setOnClickListener {
//            val question = binding.messageEditText.text.toString()
//            chatGptViewModel.addToChat(question, Message.SENT_BY_ME, chatGptViewModel.getCurrentTimestamp())
//            binding.messageEditText.setText("")
//            viewModel.callApi(question)
//        }


//        viewModel.allExp.observe(viewLifecycleOwner) {
//            viewModel.analyzer()
//        }



        // open ai close for now
        lifecycleScope.launch {
            viewModel.allExp().collect {
//                viewModel.analyzer(it)
            }
        }

        binding.meaningFrame.setOnClickListener {
//            viewModel.clearDB()
            findNavController().navigate(NavigationDirections.navigateToCategoryFragment(CATEGORY.LIFE_MEANING.name))
        }

        binding.communicationFrame.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToCategoryFragment(CATEGORY.COMMUNICATION.name))
        }

        binding.disciplineFrame.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToCategoryFragment(CATEGORY.DISCIPLINE.name))
        }

        binding.learningFrame.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToCategoryFragment(CATEGORY.LEARNING.name))
        }

        binding.workFrame.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToCategoryFragment(CATEGORY.WORK.name))
        }

        binding.relationshipFrame.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToCategoryFragment(CATEGORY.RELATIONSHIP.name))
        }


        // live streaming close for now
        val eventAdapter = EventAdapter(EventAdapter.OnClickListener {
            onSubmit()
        })

        binding.recyclerEvent.adapter = eventAdapter

        lifecycleScope.launch {
            viewModel.liveTalkEvents.collect { liveTalkEvents ->
                if (liveTalkEvents.isNotEmpty()) {
                    binding.textToJoin.setText(R.string.join_the_ongoing_live_talks)
                } else {
                    binding.textToJoin.setText(R.string.check_later_if_someone_is_giving_a_live_talk)
                }
                eventAdapter.submitList(liveTalkEvents)
            }
        }

        Log.i("HomeFragment", viewModel.categories.toString())

        viewModel.toastMessageLiveData.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        requestPermission()

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        viewModel.removeFirebaseListeners()
    }

    private fun onSubmit() {
        val channelName = "rol"
        userRole = 0
        val intent = Intent(requireActivity(), VideoActivity::class.java)
        intent.putExtra("ChannelName", channelName)
        intent.putExtra("UserRole", userRole)
        startActivity(intent)

    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.BLUETOOTH_CONNECT), 22)
    }


}
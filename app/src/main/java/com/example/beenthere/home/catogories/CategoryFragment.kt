package com.example.beenthere.home.catogories

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import com.example.beenthere.NavigationDirections
import com.example.beenthere.VideoActivity
import com.example.beenthere.data.Experience
import com.example.beenthere.data.FilteredLists
import com.example.beenthere.data.LiveTalkEvent
import com.example.beenthere.databinding.FragmentCategoryBinding

import com.example.beenthere.ext.getVmFactory
import com.example.beenthere.home.CATEGORY
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CategoryFragment : Fragment() {

    private var userRole = 0

    private lateinit var binding: FragmentCategoryBinding

    private val viewModel by viewModels<CategoryVM> { getVmFactory() }

    private var userId: String? = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val args = CategoryFragmentArgs.fromBundle(requireArguments()).category


        val linearSnapHelper = LinearSnapHelper().apply {
            attachToRecyclerView(binding.recyclerCarouse)
        }

        val adapter = CategoryAdapter(CategoryAdapter.OnClickListener {
            viewModel.navigateToDetail(it)
        })

//        val adapter = this.context?.let { ImageAdapter(it, arrayList) }
//        binding.recyclerCarouse.adapter = adapter

//        adapter?.setOnItemClickListener(object : OnItemClickListener {
//            override fun onClick(imageView: ImageView, path: String) {
////                viewModel.navigateToDetail(it)
//            }
//        })


        var meaningList: List<Experience>? = null
        var communicationList: List<Experience>? = null
        var disciplineList: List<Experience>? = null
        var learningList: List<Experience>? = null
        var workList: List<Experience>? = null
        var relationshipList: List<Experience>? = null

        binding.recyclerCarouse.adapter = adapter

        lifecycleScope.launch {
            viewModel.allExp().collect {

                meaningList = it.filter { exp ->
                    exp.category == CATEGORY.LIFE_MEANING.name
                }
                communicationList = it.filter { exp2 ->
                    exp2.category == CATEGORY.COMMUNICATION.name
                }
                disciplineList = it.filter { exp3 ->
                    exp3.category == CATEGORY.DISCIPLINE.name
                }
                learningList = it.filter { exp4 ->
                    exp4.category == CATEGORY.LEARNING.name
                }
                workList = it.filter { exp5 ->
                    exp5.category == CATEGORY.WORK.name
                }
                relationshipList = it.filter { exp6 ->
                    exp6.category == CATEGORY.RELATIONSHIP.name
                }
                adapter.submitList(
                    when (args) {
                        CATEGORY.LIFE_MEANING.name -> meaningList
                        CATEGORY.COMMUNICATION.name -> communicationList
                        CATEGORY.DISCIPLINE.name -> disciplineList
                        CATEGORY.LEARNING.name -> learningList
                        CATEGORY.WORK.name -> workList
                        else -> relationshipList
                    }
                )
            }
        }


        viewModel.navigateToDetail.observe(
            viewLifecycleOwner,
            Observer {
                it?.let {
                    findNavController().navigate(NavigationDirections.navigateToDetailFragment(it))
                    viewModel.onDetailNavigated()
                }
            }
        )

        binding.btnGoBack.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.toastMessageLiveData.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        binding.btnLaunch.setOnClickListener {
            onSubmit(
                when (args) {
                    CATEGORY.LIFE_MEANING.name -> "Life Meaning"
                    CATEGORY.COMMUNICATION.name -> "Communication"
                    CATEGORY.DISCIPLINE.name -> "Discipline"
                    CATEGORY.LEARNING.name -> "Learning"
                    CATEGORY.WORK.name -> "Work"
                    else -> "Relationship"
                }
            )
            Log.i("args",args)
        }

        return binding.root
    }

    private fun convertHttpToHttps(httpUrl: String): String {
        if (httpUrl.startsWith("http://")) {
            return httpUrl.replaceFirst("http://", "https://")
        }
        return httpUrl
    }

    private val userList =
        listOf("PollyYana", "Jammy", "Jayson", "Terry", "Elephant", "Timothy", "Bryant", "Jackson")

    private fun onSubmit(topic: String) {

        userId = userList.random()

        val channelName = "rol"
        userRole = 1
        val intent = Intent(requireActivity(), VideoActivity::class.java)
        intent.putExtra("ChannelName", channelName)
        intent.putExtra("UserRole", userRole)
        intent.putExtra("Topic", topic)
        val event = LiveTalkEvent(eventId = "", userId = userId!!, goingOn = true, topic = topic)
        val eventId = viewModel.launchLiveTalk(event)
        intent.putExtra("EventId", eventId)

        startActivity(intent)



        Log.i("Category Frag", eventId)
    }


}
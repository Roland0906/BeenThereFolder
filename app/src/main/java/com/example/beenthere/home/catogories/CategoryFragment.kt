package com.example.beenthere.home.catogories

import android.animation.Animator
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
import com.example.beenthere.R
import com.example.beenthere.VideoActivity
import com.example.beenthere.data.ExpWithCount
import com.example.beenthere.data.Experience
import com.example.beenthere.data.FilteredLists
import com.example.beenthere.data.LiveTalkEvent
import com.example.beenthere.data.toExpWithCount
import com.example.beenthere.databinding.FragmentCategoryBinding

import com.example.beenthere.ext.getVmFactory
import com.example.beenthere.home.CATEGORY
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CategoryFragment : Fragment() {

    private var userRole = 0

    private lateinit var binding: FragmentCategoryBinding

    private val viewModel by viewModels<CategoryVM> { getVmFactory() }

    private var userId: String? = ""
    private val commentCollection = Firebase.firestore.collection("comments")


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


//        var meaningList: List<Experience>? = null
//        var communicationList: List<Experience>? = null
//        var disciplineList: List<Experience>? = null
//        var learningList: List<Experience>? = null
//        var workList: List<Experience>? = null
//        var relationshipList: List<Experience>? = null
//
//        binding.recyclerCarouse.adapter = adapter
//
//        lifecycleScope.launch {
//            viewModel.allExp().collect {
//
//                meaningList = it.filter { exp ->
//                    exp.category == CATEGORY.LIFE_MEANING.name
//                }
//                communicationList = it.filter { exp2 ->
//                    exp2.category == CATEGORY.COMMUNICATION.name
//                }
//                disciplineList = it.filter { exp3 ->
//                    exp3.category == CATEGORY.DISCIPLINE.name
//                }
//                learningList = it.filter { exp4 ->
//                    exp4.category == CATEGORY.LEARNING.name
//                }
//                workList = it.filter { exp5 ->
//                    exp5.category == CATEGORY.WORK.name
//                }
//                relationshipList = it.filter { exp6 ->
//                    exp6.category == CATEGORY.RELATIONSHIP.name
//                }
//                adapter.submitList(
//                    when (args) {
//                        CATEGORY.LIFE_MEANING.name -> meaningList
//                        CATEGORY.COMMUNICATION.name -> communicationList
//                        CATEGORY.DISCIPLINE.name -> disciplineList
//                        CATEGORY.LEARNING.name -> learningList
//                        CATEGORY.WORK.name -> workList
//                        else -> relationshipList
//                    }
//                )
//            }
//        }

        var meaningList: List<Experience>? = null
        var communicationList: List<Experience>? = null
        var disciplineList: List<Experience>? = null
        var learningList: List<Experience>? = null
        var workList: List<Experience>? = null
        var relationshipList: List<Experience>? = null

        var mList: List<ExpWithCount>? = null
        var cList: List<ExpWithCount>? = null
        var dList: List<ExpWithCount>? = null
        var lList: List<ExpWithCount>? = null
        var wList: List<ExpWithCount>? = null
        var rList: List<ExpWithCount>? = null

        binding.recyclerCarouse.adapter = adapter

        playLoadingAnim()
        binding.recyclerCarouse.visibility = View.GONE
        binding.textToTalk.visibility = View.GONE
        binding.btnLaunch.visibility = View.GONE

        lifecycleScope.launch {

            viewModel.allExp().collect { it ->

                val commentCounts: MutableMap<String, Int> = mutableMapOf()

                for (exp in it) {
                    if (exp.category != "") {
                        val expId = exp.userId + exp.title + exp.situation
                        val commentsQuery = commentCollection.whereEqualTo("expId", expId)
                        commentsQuery.get().addOnSuccessListener { querySnapshot ->
                            val count = querySnapshot.size()
                            commentCounts[expId] = count

                            Log.i("Category query get", querySnapshot.size().toString())
                            Log.i("Category333", "${commentCounts[expId].toString()} | $expId")
                        }.await()
                    }

                }
                binding.lottieLoading.visibility = View.GONE
                binding.recyclerCarouse.visibility = View.VISIBLE
                binding.textToTalk.visibility = View.VISIBLE
                binding.btnLaunch.visibility = View.VISIBLE

                meaningList = it.filter { exp ->
                    exp.category == CATEGORY.LIFE_MEANING.name
                }
                mList = meaningList?.map { it.toExpWithCount() }

                val meaningExpWithCounts = mList?.map { expWithCount ->
                    Log.i("Category111", expWithCount.exp.userId)
                    Log.i("Category112", expWithCount.count.toString())
                    val commentCount =
                        commentCounts[expWithCount.exp.userId + expWithCount.exp.title + expWithCount.exp.situation]?:13
                    Log.i("Category222",expWithCount.exp.userId + expWithCount.exp.title + expWithCount.exp.situation)
                    Log.i("Catagory323", commentCount.toString())
                    expWithCount.copy(count = commentCount)
                }
                Log.i("Category444", meaningExpWithCounts.toString())

                communicationList = it.filter { exp2 ->
                    exp2.category == CATEGORY.COMMUNICATION.name
                }
                cList = communicationList?.map { it.toExpWithCount() }

                val communicateExpWithCounts = cList?.map { expWithCount ->
                    val commentCount =
                        commentCounts[expWithCount.exp.userId + expWithCount.exp.title + expWithCount.exp.situation]
                            ?: 0
                    expWithCount.copy(count = commentCount)
                }

                disciplineList = it.filter { exp3 ->
                    exp3.category == CATEGORY.DISCIPLINE.name
                }

                dList = disciplineList?.map { it.toExpWithCount() }

                val disciplineExpWithCounts = dList?.map { expWithCount ->
                    val commentCount =
                        commentCounts[expWithCount.exp.userId + expWithCount.exp.title + expWithCount.exp.situation]
                            ?: 0
                    expWithCount.copy(count = commentCount)
                }


                learningList = it.filter { exp4 ->
                    exp4.category == CATEGORY.LEARNING.name
                }
                lList = learningList?.map { it.toExpWithCount() }

                val learningExpWithCounts = lList?.map { expWithCount ->
                    val commentCount =
                        commentCounts[expWithCount.exp.userId + expWithCount.exp.title + expWithCount.exp.situation]
                            ?: 0
                    expWithCount.copy(count = commentCount)
                }

                workList = it.filter { exp5 ->
                    exp5.category == CATEGORY.WORK.name
                }
                wList = workList?.map { it.toExpWithCount() }

                val workExpWithCounts = wList?.map { expWithCount ->
                    val commentCount =
                        commentCounts[expWithCount.exp.userId + expWithCount.exp.title + expWithCount.exp.situation]
                            ?: 0
                    expWithCount.copy(count = commentCount)
                }

                relationshipList = it.filter { exp6 ->
                    exp6.category == CATEGORY.RELATIONSHIP.name
                }
                rList = relationshipList?.map { it.toExpWithCount() }

                val relationExpWithCounts = rList?.map { expWithCount ->
                    val commentCount =
                        commentCounts[expWithCount.exp.userId + expWithCount.exp.title + expWithCount.exp.situation]
                            ?: 0
                    expWithCount.copy(count = commentCount)
                }

                adapter.submitList(
                    when (args) {
                        CATEGORY.LIFE_MEANING.name -> meaningExpWithCounts
                        CATEGORY.COMMUNICATION.name -> communicateExpWithCounts
                        CATEGORY.DISCIPLINE.name -> disciplineExpWithCounts
                        CATEGORY.LEARNING.name -> learningExpWithCounts
                        CATEGORY.WORK.name -> workExpWithCounts
                        else -> relationExpWithCounts
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
            Log.i("args", args)
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

    private fun playLoadingAnim() {


        binding.lottieLoading.visibility = View.VISIBLE
        binding.lottieLoading.playAnimation()

        binding.lottieLoading.addAnimatorListener(object :
            Animator.AnimatorListener {

            override fun onAnimationStart(animation: Animator) {
                binding.lottieLoading.visibility = View.VISIBLE

            }
            override fun onAnimationEnd(animation: Animator) {
                binding.lottieLoading.visibility = View.GONE


            }
            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })
    }


}
package com.example.beenthere.home.catogories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import com.example.beenthere.NavigationDirections
import com.example.beenthere.databinding.FragmentMeaningBinding
import com.example.beenthere.ext.getVmFactory
import com.example.beenthere.home.CATEGORY
import com.example.beenthere.home.catogories.ImageAdapter.OnItemClickListener
import com.example.beenthere.home.catogories.detail.DetailFragmentArgs
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MeaningFragment : Fragment() {

    private lateinit var binding: FragmentMeaningBinding

    private val viewModel by viewModels<CategoryVM> { getVmFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMeaningBinding.inflate(inflater, container, false)
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        val arrayList: ArrayList<String> = ArrayList()

        val linearSnapHelper = LinearSnapHelper().apply {
            attachToRecyclerView(binding.recyclerCarouse)
        }

//        val adapter = CategoryAdapter(CategoryAdapter.OnClickListener {
//            viewModel.navigateToDetail(it)
//        })

        val adapter = this.context?.let { ImageAdapter(it, arrayList) }
        binding.recyclerCarouse.adapter = adapter

        adapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onClick(imageView: ImageView, path: String) {
//                viewModel.navigateToDetail(it)
            }
        })

        binding.recyclerCarouse.adapter = adapter

        lifecycleScope.launch {
            viewModel.allExp().map {
                val filteredList = it.filter { exp ->
                    exp.category == CATEGORY.LIFE_MEANING.toString()
                }
                filteredList
            }
                .collect { list ->
//                    adapter.submitList(list)
                    list.forEach {

                        arrayList.add(convertHttpToHttps(it.image.toString()))
                    }
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




        return binding.root
    }

    private fun convertHttpToHttps(httpUrl: String): String {
        if (httpUrl.startsWith("http://")) {
            return httpUrl.replaceFirst("http://", "https://")
        }
        return httpUrl
    }


}
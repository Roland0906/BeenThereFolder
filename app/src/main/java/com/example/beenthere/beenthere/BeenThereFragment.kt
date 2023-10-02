package com.example.beenthere.beenthere

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.beenthere.NavigationDirections
import com.example.beenthere.databinding.FragmentBeenThereBinding
import com.example.beenthere.ext.getVmFactory
import kotlinx.coroutines.launch


class BeenThereFragment : Fragment() {


    private lateinit var binding: FragmentBeenThereBinding

    private val viewModel by viewModels<BeenThereViewModel> { getVmFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBeenThereBinding.inflate(inflater, container, false)
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = BeenThereAdapter(BeenThereAdapter.OnClickListener {
            viewModel.navigateToAdvise(it)
        })

        binding.recyclerSituation.adapter = adapter

        lifecycleScope.launch {
            viewModel.allSituations().collect {
                adapter.submitList(it)
            }
        }

        viewModel.navigateToAdvise.observe(
            viewLifecycleOwner,
            Observer {
                it?.let {
                    findNavController().navigate(NavigationDirections.navigateToAdviseFragment(it))
                    viewModel.onAdviseNavigated()
                }
            }
        )







        return binding.root
    }

    override fun onStop() {
        super.onStop()
        viewModel.removeFirebaseListeners()
    }


}
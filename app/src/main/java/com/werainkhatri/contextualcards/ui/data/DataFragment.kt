package com.werainkhatri.contextualcards.ui.data

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.werainkhatri.contextualcards.data.repositories.DataRepository
import com.werainkhatri.contextualcards.databinding.FragmentDataBinding
import kotlinx.coroutines.InternalCoroutinesApi

class DataFragment : Fragment() {

    private var _binding: FragmentDataBinding? = null
    private val binding get() = _binding!!

    private lateinit var factory: DataViewModelFactory
    private lateinit var viewModel: DataViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    @InternalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val repository = DataRepository.getInstance()
        factory = DataViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(DataViewModel::class.java)
        viewModel.updateData()
        Log.d(tag, "Name: ${viewModel.data.value?.get(0)?.name}")
        viewModel.data.observe(viewLifecycleOwner, { data ->
             binding.rvCardGroups.also {
                it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                it.setHasFixedSize(true)
                it.adapter = DataAdapter(data, this, requireContext())
            }
        })
    }

}
package com.werainkhatri.contextualcards.ui.data

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.werainkhatri.contextualcards.R
import com.werainkhatri.contextualcards.data.repositories.DataRepository
import com.werainkhatri.contextualcards.databinding.FragmentDataBinding
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class DataFragment : Fragment() {

    private var _binding: FragmentDataBinding? = null
    private val binding get() = _binding!!

    private lateinit var factory: DataViewModelFactory
    private lateinit var viewModel: DataViewModel
    private lateinit var prefs: SharedPreferences

    private val toSkip: MutableSet<Int> = mutableSetOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataBinding.inflate(inflater, container, false)
        prefs =
            activity?.getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
            )!!
        toSkip.addAll(
            prefs.getStringSet(getString(R.string.dismissed_list), mutableSetOf())
                ?.map(String::toInt)?.toList()!!
        )
        binding.swipeRefresh.setOnRefreshListener {
            getData()
            binding.shimmer.startShimmer()
            binding.shimmer.visibility = View.VISIBLE
            binding.rvCardGroups.visibility = View.GONE
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getData()
    }

    private fun getData() {
        val repository = DataRepository.getInstance()
        factory = DataViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(DataViewModel::class.java)
        viewModel.updateData(toSkip) {
            binding.swipeRefresh.isRefreshing = false
            binding.shimmer.stopShimmer()
            binding.shimmer.visibility = View.GONE
            binding.rvCardGroups.visibility = View.VISIBLE
            it?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
        viewModel.data.observe(viewLifecycleOwner, { data ->
            binding.rvCardGroups.also {
                it.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                it.setHasFixedSize(true)
                it.adapter =
                    DataAdapter(data.toMutableList(), this, requireContext()) { i, dismiss ->
                        toSkip.add(data[i].id)
                        if (dismiss) {
                            prefs.edit {
                                this.putStringSet(
                                    getString(R.string.dismissed_list),
                                    prefs.getStringSet(
                                        getString(R.string.dismissed_list),
                                        mutableSetOf()
                                    )?.union(setOf(data[i].id.toString()))
                                )
                                this.apply()
                                this.commit()
                            }
                        }
                        viewModel.removeAt(i)
                    }
            }
        })
    }

}
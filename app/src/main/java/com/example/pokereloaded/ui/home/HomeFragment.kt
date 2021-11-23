package com.example.pokereloaded.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pokereloaded.R
import com.example.pokereloaded.databinding.FragmentHomeBinding
import com.example.pokereloaded.extentions.hideKeyboard
import com.example.pokereloaded.models.Pokemon
import kotlinx.coroutines.flow.collectLatest

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter : PokemonAdapter
    private lateinit var paginatedAdapter: PaginatedAdapter
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMembers()
        setupRecycler()
        setEditText()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.pokemonList.observe(viewLifecycleOwner, { getList(it) })
    }

    private fun setEditText(){
        this.view?.rootView?.let { this.hideKeyboard(it) }
        binding.searchEditText.setOnEditorActionListener { _, actionId, _ -> editorNameActionListener(actionId)  }
    }

    private fun editorNameActionListener(actionId: Int): Boolean {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            binding.recycler.adapter = adapter
            adapter.setList(viewModel.list.filter { binding.searchEditText.text?.let { text ->
                it.name?.contains(text) } ?: false })
            this.view?.rootView?.let { this.hideKeyboard(it) }
            return true
        }
        return false
    }

    private fun getList(pokemonList: List<Pokemon>?) {
       pokemonList
           ?.let { /* Do Nothing */ }
           ?:run { Toast.makeText(context, getString(R.string.error_list), Toast.LENGTH_LONG).show() }
    }

    private fun initMembers() {
        adapter = PokemonAdapter()
        paginatedAdapter = PaginatedAdapter()
        paginatedAdapter.onPokemonClickCallback = { navigateToDetail(it) }
        adapter.onPokemonClickCallback = { navigateToDetail(it) }
        viewModel.fetchList()
        fetchPaginatedList()
    }

    private fun fetchPaginatedList() {
        lifecycleScope.launchWhenCreated {
            viewModel.fetchPaginatedList().collectLatest {
                paginatedAdapter.submitData(it)
            }
        }
    }

    private fun navigateToDetail(pokemon: Pokemon) {
        val bundle = Bundle()
        bundle.putParcelable(getString(R.string.pokemon), pokemon)
        findNavController().navigate(R.id.navigation_detail, bundle)
    }

    private fun setupRecycler() {
        binding.recycler.layoutManager = GridLayoutManager(context, 3)
        binding.recycler.adapter = paginatedAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
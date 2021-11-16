package com.example.pokereloaded.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pokereloaded.R
import com.example.pokereloaded.databinding.FragmentHomeBinding
import com.example.pokereloaded.extentions.hideKeyboard
import com.example.pokereloaded.models.Pokemon

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter : PokemonAdapter
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
        binding.searchEditText.setOnEditorActionListener { _, actionId, _ -> editorNameActionListener(actionId)  }
    }

    private fun editorNameActionListener(actionId: Int): Boolean {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            adapter.setList(viewModel.list.filter { binding.searchEditText.text?.let { text ->
                it.name?.contains(text) } ?: false })
            this.view?.rootView?.let { this.hideKeyboard(it) }
            return true
        }
        return false
    }

    private fun getList(pokemonList: List<Pokemon>?) {
       pokemonList
           ?.let { adapter.setList(it) }
           ?:run { Toast.makeText(context, getString(R.string.error_list), Toast.LENGTH_LONG).show() }
    }

    private fun initMembers() {
        adapter = PokemonAdapter()
        adapter.onPokemonClickCallback = { navigateToDetail(it) }
        viewModel.fetchList()
    }

    private fun navigateToDetail(pokemon: Pokemon) {
        val bundle = Bundle()
        bundle.putParcelable(getString(R.string.pokemon), pokemon)
        findNavController().navigate(R.id.navigation_detail, bundle)
    }

    private fun setupRecycler() {
        binding.recycler.layoutManager = GridLayoutManager(context, 3)
        binding.recycler.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
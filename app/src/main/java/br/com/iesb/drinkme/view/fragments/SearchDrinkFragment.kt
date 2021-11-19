package br.com.iesb.drinkme.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import br.com.iesb.drinkme.databinding.FragmentSearchDrinkBinding
import br.com.iesb.drinkme.model.DrinkRepository
import br.com.iesb.drinkme.model.network.RetrofitInstance
import br.com.iesb.drinkme.util.toggleVisibility
import br.com.iesb.drinkme.view.adapters.DrinkAdapter
import br.com.iesb.drinkme.viewmodel.SearchDrinkViewModel
import br.com.iesb.drinkme.viewmodel.factories.SearchDrinkViewModelFactory

class SearchDrinkFragment : Fragment() {

    private var _binding: FragmentSearchDrinkBinding? = null
    private val binding: FragmentSearchDrinkBinding get() = _binding!!
    private lateinit var searchAdapter: DrinkAdapter

    private val viewModel by lazy {
        val repository = DrinkRepository(RetrofitInstance.apiService)
        val viewModelFactory = SearchDrinkViewModelFactory(repository)
        ViewModelProvider(this, viewModelFactory)[SearchDrinkViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchDrinkBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        doSearch()

        loading()

        setupSearchRv()

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        goToDetails()

        searchAdapter.setOnLongItemClickListener {
            false
        }
    }

    private fun goToDetails() {
        searchAdapter.setOnClickListener {
            findNavController().navigate(
                SearchDrinkFragmentDirections.actionSearchDrinkFragmentToDrinkDetailsFrament(
                    it.idDrink
                )
            )
        }
    }

    private fun doSearch() {
        binding.buttonDoSearch.setOnClickListener {
            if (binding.drinkSearch.text.isNullOrEmpty().not()) {
                viewModel.searchDrink(binding.drinkSearch.text.toString())
            } else {
                binding.drinkSearch.error = "Can't be empty"
            }
        }
    }

    private fun loading() {
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.searchedDrinkRv.toggleVisibility(false)
                binding.loadinProgressBar.toggleVisibility(true)
            } else {
                binding.searchedDrinkRv.toggleVisibility(true)
                binding.loadinProgressBar.toggleVisibility(false)
            }
        }
    }

    private fun setupSearchRv() {
        searchAdapter = DrinkAdapter()
        binding.searchedDrinkRv.apply {
            adapter = searchAdapter
            layoutManager = GridLayoutManager(context, 2)
        }

        viewModel.searchedDrinksLiveData.observe(viewLifecycleOwner) {
            searchAdapter.differ.submitList(it)
        }
    }

}
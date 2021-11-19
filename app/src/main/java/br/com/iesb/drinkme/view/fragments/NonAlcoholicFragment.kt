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
import br.com.iesb.drinkme.databinding.FragmentNonAlcoholicBinding
import br.com.iesb.drinkme.model.DrinkRepository
import br.com.iesb.drinkme.model.network.RetrofitInstance
import br.com.iesb.drinkme.util.toggleVisibility
import br.com.iesb.drinkme.view.adapters.DrinkAdapter
import br.com.iesb.drinkme.viewmodel.NonAlcoholicViewModel
import br.com.iesb.drinkme.viewmodel.factories.NonAlcoholicViewModelFactory


class NonAlcoholicFragment : Fragment() {

    private var _binding: FragmentNonAlcoholicBinding? = null
    private val binding: FragmentNonAlcoholicBinding get() = _binding!!
    private lateinit var nonAlcoholicAdapter: DrinkAdapter

    private val viewModel by lazy {
        val viewModelFactory =
            NonAlcoholicViewModelFactory(repository = DrinkRepository(RetrofitInstance.apiService))
        ViewModelProvider(this, viewModelFactory)[NonAlcoholicViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNonAlcoholicBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getNonAlcoholicDrinks()

        setupNonAlcoholicRv()

        loading()

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        goToDetails()

    }

    private fun goToDetails() {
        nonAlcoholicAdapter.setOnClickListener {
            findNavController().navigate(
                NonAlcoholicFragmentDirections.actionNonAlcoholicFragmentToDrinkDetailsFrament(
                    it.idDrink
                )
            )
        }
    }

    private fun loading() {
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.nonAlcoholicRv.toggleVisibility(false)
                binding.loadinProgressBar.toggleVisibility(true)
            } else {
                binding.nonAlcoholicRv.toggleVisibility(true)
                binding.loadinProgressBar.toggleVisibility(false)
            }
        }
    }

    private fun setupNonAlcoholicRv() {
        nonAlcoholicAdapter = DrinkAdapter()
        binding.nonAlcoholicRv.apply {
            adapter = nonAlcoholicAdapter
            layoutManager = GridLayoutManager(context, 2)
        }

        viewModel.nonAlcoholicLiveData.observe(viewLifecycleOwner) { listOfDrinks ->
            nonAlcoholicAdapter.differ.submitList(listOfDrinks)

        }
    }


}
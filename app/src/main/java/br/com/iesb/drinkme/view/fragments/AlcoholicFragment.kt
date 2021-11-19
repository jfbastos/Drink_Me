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
import br.com.iesb.drinkme.databinding.FragmentAlcoholicBinding
import br.com.iesb.drinkme.model.DrinkRepository
import br.com.iesb.drinkme.model.network.RetrofitInstance
import br.com.iesb.drinkme.util.toggleVisibility
import br.com.iesb.drinkme.view.adapters.DrinkAdapter
import br.com.iesb.drinkme.viewmodel.AlcoholicViewModel
import br.com.iesb.drinkme.viewmodel.factories.AlcoholicViewModelFactory

class AlcoholicFragment : Fragment() {

    private var _binding: FragmentAlcoholicBinding? = null
    private val binding: FragmentAlcoholicBinding get() = _binding!!
    private lateinit var drinkAdapter: DrinkAdapter

    private val viewModel: AlcoholicViewModel by lazy {
        val viewModelProviderFactory =
            AlcoholicViewModelFactory(repository = DrinkRepository(RetrofitInstance.apiService))
        ViewModelProvider(this, viewModelProviderFactory)[AlcoholicViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlcoholicBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getDrinks()

        setupAlcoholicRv()

        loading()

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        goToDetails()
    }

    private fun goToDetails() {
        drinkAdapter.setOnClickListener {
            findNavController().navigate(
                AlcoholicFragmentDirections.actionAlcoholicFragmentToDrinkDetailsFrament(
                    drinkId = it.idDrink
                )
            )
        }
    }

    private fun loading() {
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.alcoholicRv.toggleVisibility(false)
                binding.loadinProgressBar.toggleVisibility(true)
            } else {
                binding.alcoholicRv.toggleVisibility(true)
                binding.loadinProgressBar.toggleVisibility(false)
            }
        }
    }

    private fun setupAlcoholicRv() {
        drinkAdapter = DrinkAdapter()
        binding.alcoholicRv.apply {
            adapter = drinkAdapter
            layoutManager = GridLayoutManager(context, 2)
        }

        viewModel.drinkLiveData.observe(viewLifecycleOwner) { listOfDrinks ->
            drinkAdapter.differ.submitList(listOfDrinks)
        }

    }


}
package br.com.iesb.drinkme.view.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import br.com.iesb.drinkme.databinding.FragmentFavoritesBinding
import br.com.iesb.drinkme.model.DrinkRepository
import br.com.iesb.drinkme.model.GetListOfDrinks
import br.com.iesb.drinkme.model.network.RetrofitInstance
import br.com.iesb.drinkme.view.adapters.DrinkAdapter
import br.com.iesb.drinkme.viewmodel.FavoritesViewModel
import br.com.iesb.drinkme.viewmodel.factories.FavoritesViewModelFactory


class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding: FragmentFavoritesBinding get() = _binding!!
    private lateinit var favoritesAdapter: DrinkAdapter

    private val viewModel by lazy {
        val viewModelFactory =
            FavoritesViewModelFactory(repository = DrinkRepository(RetrofitInstance.apiService))
        ViewModelProvider(this, viewModelFactory)[FavoritesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupFavoritesRv()

        goToDetails()

        favoritesAdapter.setOnLongItemClickListener {
            confirmDeletion(it)
            true
        }
    }

    private fun goToDetails() {
        favoritesAdapter.setOnClickListener {
            findNavController().navigate(
                FavoritesFragmentDirections.actionFavoritesFragmentToDrinkDetailsFrament(
                    it.idDrink
                )
            )
        }
    }

    private fun confirmDeletion(it: GetListOfDrinks.Drink) {
        AlertDialog.Builder(context).apply {
            setTitle("Delete drink")
            setMessage("Are sure you want to delete ${it.strDrink}?")
            setIcon(android.R.drawable.ic_dialog_alert)
            setPositiveButton("Yes") { _, _ ->
                viewModel.removeFromFavorites(it.idDrink)
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
            }
            setNegativeButton("Cancel") { _, _ ->

            }
            create()
        }.show()
    }

    private fun setupFavoritesRv() {
        favoritesAdapter = DrinkAdapter()
        binding.favoritesRv.apply {
            adapter = favoritesAdapter
            layoutManager = GridLayoutManager(context, 2)
        }
        viewModel.drinksLiveData.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.favoritesRv.visibility = View.GONE
                binding.emptyList.root.visibility = View.VISIBLE
            } else {
                binding.favoritesRv.visibility = View.VISIBLE
                binding.emptyList.root.visibility = View.GONE
                favoritesAdapter.differ.submitList(it)
            }

        }


    }

}
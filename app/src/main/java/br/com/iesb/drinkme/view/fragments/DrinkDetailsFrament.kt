package br.com.iesb.drinkme.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.com.iesb.drinkme.databinding.FragmentDrinkDetailsFramentBinding
import br.com.iesb.drinkme.model.DrinkRepository
import br.com.iesb.drinkme.model.network.RetrofitInstance
import br.com.iesb.drinkme.util.toggleVisibility
import br.com.iesb.drinkme.viewmodel.DrinkDetailsViewModel
import br.com.iesb.drinkme.viewmodel.factories.DrinkDetailsViewModelFactory
import coil.load


class DrinkDetailsFrament : Fragment() {

    private var _binding: FragmentDrinkDetailsFramentBinding? = null
    private val binding: FragmentDrinkDetailsFramentBinding get() = _binding!!

    private val args: DrinkDetailsFramentArgs by lazy {
        DrinkDetailsFramentArgs.fromBundle(requireArguments())
    }

    private val viewModel by lazy {
        val viewModelFactory =
            DrinkDetailsViewModelFactory(repository = DrinkRepository(RetrofitInstance.apiService))
        ViewModelProvider(this, viewModelFactory)[DrinkDetailsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDrinkDetailsFramentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getDrinkDetail(args.drinkId)

        loading()

        viewModel.drinkLiveData.observe(viewLifecycleOwner) { drink ->
            fillViewData(drink)
            favoriteDrink(drink)
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun favoriteDrink(drink: Map<String, String?>) {
        binding.addFavButton.setOnClickListener {
            viewModel.addToFavorites(
                drink["strDrink"]!!,
                drink["strDrinkThumb"]!!,
                drink["idDrink"]!!
            )
            Toast.makeText(context, "Drink favorited", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fillViewData(drink: Map<String, String?>) {
        binding.drinkImage.load(drink["strDrinkThumb"])
        binding.drinkName.text = drink["strDrink"]
        binding.drinkInstructions.text = drink["strInstructions"]
        binding.drinkIngredient1.text =
            formatIngredientsText(drink["strIngredient1"], drink["strMeasure1"])
        binding.drinkIngredient2.text =
            formatIngredientsText(drink["strIngredient2"], drink["strMeasure2"])
        binding.drinkIngredient3.text =
            formatIngredientsText(drink["strIngredient3"], drink["strMeasure3"])
        binding.drinkIngredient4.text =
            formatIngredientsText(drink["strIngredient4"], drink["strMeasure4"])
        binding.drinkIngredient5.text =
            formatIngredientsText(drink["strIngredient5"], drink["strMeasure5"])
        binding.drinkIngredient6.text =
            formatIngredientsText(drink["strIngredient6"], drink["strMeasure6"])
        binding.drinkIngredient7.text =
            formatIngredientsText(drink["strIngredient7"], drink["strMeasure7"])
        binding.drinkIngredient8.text =
            formatIngredientsText(drink["strIngredient8"], drink["strMeasure8"])
        binding.drinkIngredient9.text =
            formatIngredientsText(drink["strIngredient9"], drink["strMeasure9"])
        binding.drinkIngredient10.text =
            formatIngredientsText(drink["strIngredient10"], drink["strMeasure10"])
    }

    private fun formatIngredientsText(ingredient: String?, measure: String?): String {
        if (ingredient != null) {
            if (measure != null) {
                return "$ingredient -> $measure"
            }
            return "$ingredient"
        } else {
            return ""
        }
    }

    private fun loading() {
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.mainView.toggleVisibility(false)
                binding.loadinProgressBar.toggleVisibility(true)
            } else {
                binding.mainView.toggleVisibility(true)
                binding.loadinProgressBar.toggleVisibility(false)
            }
        }
    }
}
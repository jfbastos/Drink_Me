package br.com.iesb.drinkme.viewmodel

import androidx.lifecycle.ViewModel
import br.com.iesb.drinkme.model.DrinkRepository

class FavoritesViewModel(val repository: DrinkRepository) : ViewModel() {

    val drinksLiveData = repository.getDrinks()

    fun removeFromFavorites(drinkId: String) {
        repository.removeFavorite(drinkId)
    }
}
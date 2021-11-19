package br.com.iesb.drinkme.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.iesb.drinkme.model.DrinkRepository
import kotlinx.coroutines.launch

class DrinkDetailsViewModel(private val repository: DrinkRepository) : ViewModel() {

    private var _drinkLiveData = MutableLiveData<Map<String, String?>>()
    val drinkLiveData: LiveData<Map<String, String?>> get() = _drinkLiveData

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private var _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun getDrinkDetail(drinkId: String) = viewModelScope.launch {
        _loading.postValue(true)
        val response = repository.getDrinkById(drinkId)

        if (response.isSuccessful) {
            val drink = response.body()
            if (drink != null) {
                _drinkLiveData.postValue(drink.drinks[0])
                _loading.postValue(false)
            } else {
                _error.postValue("Problem on get drink details")
                _loading.postValue(false)
            }
        } else {
            _error.postValue("Problem on get drink details")
            _loading.postValue(false)
        }
    }

    fun addToFavorites(drinkName: String, drinkThumb: String, drinkId: String) {
        repository.saveDrink(name = drinkName, thumb = drinkThumb, id = drinkId)
    }
}
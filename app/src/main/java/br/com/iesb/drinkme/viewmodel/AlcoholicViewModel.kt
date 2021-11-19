package br.com.iesb.drinkme.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.iesb.drinkme.model.DrinkRepository
import br.com.iesb.drinkme.model.GetListOfDrinks
import kotlinx.coroutines.launch

class AlcoholicViewModel(private val repository: DrinkRepository) : ViewModel() {

    private var _drinkLiveData = MutableLiveData<List<GetListOfDrinks.Drink>>()
    val drinkLiveData: LiveData<List<GetListOfDrinks.Drink>> get() = _drinkLiveData

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private var _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error


    fun getDrinks() = viewModelScope.launch {
        _loading.postValue(true)
        val response = repository.getAlcoholicDrinks()

        if (response.isSuccessful) {
            val listOfDrinks = response.body()
            if(listOfDrinks == null){
                _error.postValue("Problem to get alcoholics drinks")
                _loading.postValue(false)
            }else{
                _drinkLiveData.postValue(listOfDrinks.drinks)
                _loading.postValue(false)
            }
        } else {
            _error.postValue("Problem to get alcoholics drinks")
            _loading.postValue(false)
        }
    }

}
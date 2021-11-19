package br.com.iesb.drinkme.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.iesb.drinkme.model.DrinkRepository
import br.com.iesb.drinkme.model.GetListOfDrinks
import kotlinx.coroutines.launch

class NonAlcoholicViewModel(private val repository: DrinkRepository) : ViewModel() {

    private val _nonAlcoholicLiveData = MutableLiveData<List<GetListOfDrinks.Drink>>()
    val nonAlcoholicLiveData: LiveData<List<GetListOfDrinks.Drink>> get() = _nonAlcoholicLiveData

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private var _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun getNonAlcoholicDrinks() = viewModelScope.launch {
        _loading.postValue(true)
        val response = repository.getNonAlcoholicDrinks()

        if (response.isSuccessful) {
            val listOfDrinks = response.body()
            if(listOfDrinks == null){
                _error.postValue("Problem to get non alcoholics drinks")
                _loading.postValue(false)
            }else{
                _nonAlcoholicLiveData.postValue(listOfDrinks.drinks)
                _loading.postValue(false)
            }
        } else {
            _error.postValue("Problem to get non alcoholics drinks")
            _loading.postValue(false)
        }
    }
}
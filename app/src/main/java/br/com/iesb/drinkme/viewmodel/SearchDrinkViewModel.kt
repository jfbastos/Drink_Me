package br.com.iesb.drinkme.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.iesb.drinkme.model.DrinkRepository
import br.com.iesb.drinkme.model.GetListOfDrinks
import kotlinx.coroutines.launch

class SearchDrinkViewModel(val repository: DrinkRepository) : ViewModel() {

    private var _searchedDrinksLiveData = MutableLiveData<List<GetListOfDrinks.Drink>>()
    val searchedDrinksLiveData: LiveData<List<GetListOfDrinks.Drink>> get() = _searchedDrinksLiveData

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private var _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun searchDrink(drinkStr: String) = viewModelScope.launch {
        _loading.postValue(true)
        val response = repository.getDrinkByName(drinkStr)

        if (response.isSuccessful) {
            val listOfDrinks = response.body()
            if(listOfDrinks != null){
                val drinks = arrayListOf<GetListOfDrinks.Drink>()
                if(listOfDrinks.drinks.isNullOrEmpty().not()){
                    for (i in 0 until (listOfDrinks.drinks.size)) {
                        val drinkName = listOfDrinks.drinks[i]["strDrink"]
                        val drinkThumb = listOfDrinks.drinks[i]["strDrinkThumb"]
                        val drinkId = listOfDrinks.drinks[i]["idDrink"]

                        if (drinkName.isNullOrEmpty().not() || drinkThumb.isNullOrEmpty()
                                .not() || drinkId.isNullOrEmpty().not()
                        ) {
                            drinks.add(GetListOfDrinks.Drink(drinkName!!, drinkThumb!!, drinkId!!))
                        }
                    }
                }else{
                    _error.postValue("Drink not found!")
                    _loading.postValue(false)
                }
                _searchedDrinksLiveData.postValue(drinks)
                _loading.postValue(false)
            }else{
                _error.postValue("Error on get drink")
                _loading.postValue(false)
            }
        }else{
            _error.postValue("Error on get drink")
            _loading.postValue(false)
        }
    }
}
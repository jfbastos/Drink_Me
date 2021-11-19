package br.com.iesb.drinkme.viewmodel.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.iesb.drinkme.model.DrinkRepository
import br.com.iesb.drinkme.viewmodel.AlcoholicViewModel

class AlcoholicViewModelFactory (private val repository: DrinkRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlcoholicViewModel(repository = repository) as T
    }


}
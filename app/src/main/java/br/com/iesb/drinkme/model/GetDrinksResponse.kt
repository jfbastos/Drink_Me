package br.com.iesb.drinkme.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class GetListOfDrinks(val drinks: List<Drink>) {

    @Entity(tableName = "drinks")
    data class Drink(
        val strDrink: String = "",
        val strDrinkThumb: String = "",
        @PrimaryKey val idDrink: String
    )
}
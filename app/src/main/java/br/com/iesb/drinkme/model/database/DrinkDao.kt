package br.com.iesb.drinkme.model.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.iesb.drinkme.model.GetListOfDrinks

@Dao
interface DrinkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDrink(drink : GetListOfDrinks.Drink)

    @get: Query("SELECT * FROM drinks")
    val drinks : LiveData<List<GetListOfDrinks.Drink>>

    @Query("DELETE FROM drinks WHERE idDrink = :idDrink")
    fun delete(idDrink : String)

}
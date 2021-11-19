package br.com.iesb.drinkme.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.iesb.drinkme.model.GetListOfDrinks


@Database(entities = [GetListOfDrinks.Drink::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun DrinkDao() : DrinkDao
}
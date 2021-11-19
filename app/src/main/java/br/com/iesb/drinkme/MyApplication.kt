package br.com.iesb.drinkme

import android.app.Application
import androidx.room.Room
import br.com.iesb.drinkme.model.database.AppDatabase

class MyApplication : Application() {

    companion object {
        var database: AppDatabase? = null
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "drink_db")
            .fallbackToDestructiveMigration()
            .build()
    }
}
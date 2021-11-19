package br.com.iesb.drinkme.model

import androidx.lifecycle.LiveData
import br.com.iesb.drinkme.MyApplication
import br.com.iesb.drinkme.model.network.CocktailApi
import br.com.iesb.drinkme.model.network.SafeApiCall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class DrinkRepository(private val service : CocktailApi) {

    suspend fun getAlcoholicDrinks(): Response<GetListOfDrinks> {
        return SafeApiCall.safeNetworkRequest {
            service.getAlcoholicDrinks()
        } ?: Response.success(null)
    }

    suspend fun getNonAlcoholicDrinks(): Response<GetListOfDrinks> {
        return SafeApiCall.safeNetworkRequest {
            service.getNonAlcoholicDrinks()
        } ?: Response.success(null)
    }

    suspend fun getDrinkById(drinkId: String): Response<GetDrinkDetailsResponse> {
        return SafeApiCall.safeNetworkRequest {
            service.getDrinkById(drinkId)
        } ?: Response.success(null)
    }

    suspend fun getDrinkByName(drinkName : String) : Response<GetDrinkDetailsResponse>{
        return SafeApiCall.safeNetworkRequest {
            service.getDrinkByName(drinkName)
        } ?: Response.success(null)
    }

    fun saveDrink(thumb: String, name: String, id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            MyApplication.database!!.DrinkDao().insertDrink(GetListOfDrinks.Drink(name, thumb, id))
        }
    }

    fun getDrinks(): LiveData<List<GetListOfDrinks.Drink>> {
        return MyApplication.database!!.DrinkDao().drinks
    }

    fun removeFavorite(drinkId : String){
        CoroutineScope(Dispatchers.IO).launch {
            MyApplication.database!!.DrinkDao().delete(drinkId)
        }
    }

}
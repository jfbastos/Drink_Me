package br.com.iesb.drinkme.model.network

import br.com.iesb.drinkme.model.GetDrinkDetailsResponse
import br.com.iesb.drinkme.model.GetListOfDrinks
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailApi {

    companion object{
        const val BASE_URL = "https://www.thecocktaildb.com/api/json/v1/1/"
    }

    @GET("filter.php?a=Alcoholic")
    suspend fun getAlcoholicDrinks() : Response<GetListOfDrinks>

    @GET("filter.php?a=Non_Alcoholic")
    suspend fun getNonAlcoholicDrinks() : Response<GetListOfDrinks>

    @GET("lookup.php")
    suspend fun getDrinkById(
        @Query("i") drinkId: String
    ) : Response<GetDrinkDetailsResponse>

    @GET("search.php")
    suspend fun getDrinkByName(
        @Query("s") drinkName : String
    ) : Response<GetDrinkDetailsResponse>

}
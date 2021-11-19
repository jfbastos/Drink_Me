package br.com.iesb.drinkme.model.network

object SafeApiCall {

    suspend fun <T> safeNetworkRequest(
        request: suspend () -> T
    ): T? {
        return try {
            request()
        }catch (e : Exception){
            println(e.localizedMessage)
            null
        }
    }

}
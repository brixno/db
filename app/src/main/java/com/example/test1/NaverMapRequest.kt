import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NaverMapRequest {
    const val BASE_URL = "http://myhosting.dothome.co.kr/"

    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}
package mekotlinapps.dnyaneshwar.`in`.restdemo.rest

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor



/**
 * Created by Dnyaneshwar Dalvi on 21/11/17.
 */
class APIClient {


    companion object {

        val baseURL: String = "https://api.themoviedb.org/3/"
        var retofit: Retrofit? = null

        val client: Retrofit
            get() {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                val client = OkHttpClient.Builder()
                        .addInterceptor(interceptor).build()
                if (retofit == null) {
                    retofit = Retrofit.Builder().client(client)
                            .baseUrl(baseURL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                }
                return retofit!!
            }
    }
}
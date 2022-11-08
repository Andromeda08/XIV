package hu.tsukiakari.xiv.network

import android.util.Log
import hu.tsukiakari.xiv.network.model.character.CharacterResult
import hu.tsukiakari.xiv.network.model.lodestone.LodestoneCharacter
import hu.tsukiakari.xiv.network.model.lodestone.LodestoneResponse
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

object XIVApi {
    private val retrofit: Retrofit
    private val characterApi: CharacterApi
    private val private_key: String = "0cf69f171f7942938ccc2536721b66572ce6f18e06514b4d9ff3b3c00326adc4"

    init {
        retrofit = Retrofit.Builder()
            .baseUrl("https://xivapi.com/")
            .client(OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val url = chain.request().url().newBuilder()
                        .addQueryParameter("private_key", private_key)
                        .build()
                    chain.proceed(chain.request().newBuilder().url(url).build())
                }
                .addInterceptor { chain ->
                    Log.i("Url", chain.request().url().toString())
                    val req = chain.request()
                    chain.proceed(req)
                }.build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        characterApi = retrofit.create(CharacterApi::class.java)
    }

    fun getCharacter(name: String, server: String): Call<CharacterResult?>? {
        val n = name.replace(" ", "+")
        Log.i("API Call:", "$n, $server")
        return characterApi.getCharacter(n, server)
    }

    fun getLodestone(id: Long): Call<LodestoneResponse?>? {
        Log.i("API Call", "getLodestone($id)")
        return characterApi.getLodestone(id, 1)
    }
}
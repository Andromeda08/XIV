package hu.tsukiakari.xiv.network

import android.util.Log
import hu.tsukiakari.xiv.network.model.character.CharacterResult
import hu.tsukiakari.xiv.network.model.lodestone.LodestoneCharacter
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

object XIVApi {
    private val retrofit: Retrofit
    private val characterApi: CharacterApi

    init {
        retrofit = Retrofit.Builder()
            .baseUrl("https://xivapi.com/")
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        characterApi = retrofit.create(CharacterApi::class.java)
    }

    fun getCharacter(name: String, server: String): Call<CharacterResult?>? {
        val n = name.replace(" ", "+")
        Log.d("API Call:", "$n, $server")
        return characterApi.getCharacter(n, server)
    }

    fun getLodestone(id: Long): Call<LodestoneCharacter?>? {
        return characterApi.getLodestone(id.toString())
    }
}
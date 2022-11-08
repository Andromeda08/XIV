package hu.tsukiakari.xiv.network

import hu.tsukiakari.xiv.network.model.character.CharacterResult
import hu.tsukiakari.xiv.network.model.lodestone.LodestoneResponse
import retrofit2.Call
import retrofit2.http.*

interface CharacterApi {
    @GET("character/search")
    fun getCharacter(
        @Query("name") characterName: String,
        @Query("server") server: String,
    ): Call<CharacterResult?>?

    @GET("character/{id}")
    fun getLodestone(
        @Path("id") id: Long,
        @Query("extended") value: Int
    ): Call<LodestoneResponse?>?
}
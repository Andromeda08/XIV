package hu.tsukiakari.xiv.network.model.lodestone

import com.google.gson.annotations.SerializedName

data class LodestoneResponse(
    @SerializedName("Character")
    val character: LodestoneCharacter
)

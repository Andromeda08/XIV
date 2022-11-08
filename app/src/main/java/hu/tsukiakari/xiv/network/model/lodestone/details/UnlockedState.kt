package hu.tsukiakari.xiv.network.model.lodestone.details

import com.google.gson.annotations.SerializedName

data class UnlockedState(
    val ID: Int,

    @SerializedName("Name")
    val name: String
)

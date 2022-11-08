package hu.tsukiakari.xiv.network.model.lodestone.details

import com.google.gson.annotations.SerializedName

data class GrandCompany(
    @SerializedName("Company")
    val company: Company?,

    @SerializedName("Rank")
    val rank: Rank?
)

data class Company(
    val ID: Int,
    val Name: String
)

data class Rank(
    val ID: Int,
    val Name: String,
    val Icon: String
)

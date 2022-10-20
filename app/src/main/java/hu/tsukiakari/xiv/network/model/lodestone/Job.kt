package hu.tsukiakari.xiv.network.model.lodestone

import com.google.gson.annotations.SerializedName

data class Job(
    @SerializedName("ClassID")
    val classID: Int,

    @SerializedName("ExpLevel")
    val expLevel: Double,

    @SerializedName("ExpLevelMax")
    val expLevelMax: Double,

    @SerializedName("ExpLevelTogo")
    val expLevelTogo: Double,

    @SerializedName("IsSpecialized")
    val isSpecialised: Boolean,

    @SerializedName("JobID")
    val jobID: Int,

    @SerializedName("Level")
    val level: Int,

    @SerializedName("Name")
    val name: String,

    @SerializedName("UnlockedState")
    val unlockedState: UnlockedState
)

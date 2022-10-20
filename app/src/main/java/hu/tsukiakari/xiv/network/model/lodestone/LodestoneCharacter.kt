package hu.tsukiakari.xiv.network.model.lodestone

import com.google.gson.annotations.SerializedName

data class LodestoneCharacter(
    @SerializedName("ActiveClassJob")
    val activeClassJob: Job,

    @SerializedName("Avatar")
    val avatar: String,

    @SerializedName("Bio")
    val bio: String,

    @SerializedName("ClassJobs")
    val classJobs: List<Job>,

    @SerializedName("DC")
    val dataCenter: String,

    @SerializedName("FreeCompanyId")
    val freeCompanyID: Long?,

    @SerializedName("FreeCompanyName")
    val freeCompanyName: String?,

    @SerializedName("Gender")
    val gender: Int,

    //@SerializedName("GrandCompany")
    //val grandCompany: GrandCompany,

    val ID: Long,

    @SerializedName("Lang")
    val language: String,

    @SerializedName("Name")
    val name: String,

    @SerializedName("Nameday")
    val nameday: String,

    @SerializedName("Portrait")
    val portrait: String,

    @SerializedName("Race")
    val race: Int,

    @SerializedName("Server")
    val server: String,
)

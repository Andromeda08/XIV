package hu.tsukiakari.xiv.network.model.lodestone

import com.google.gson.annotations.SerializedName
import hu.tsukiakari.xiv.network.model.lodestone.details.*

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

    @SerializedName("Server")
    val server: String,

    @SerializedName("Gender")
    val gender: Int,

    val ID: Long,

    @SerializedName("Lang")
    val language: String,

    @SerializedName("Name")
    val name: String,

    @SerializedName("Nameday")
    val nameday: String,

    @SerializedName("Portrait")
    val portrait: String,

    @SerializedName("GrandCompany")
    val grandCompany: GrandCompany,

    @SerializedName("GuardianDeity")
    val guardianDeity: GuardianDeity,

    @SerializedName("Race")
    val race: Race,

    @SerializedName("Title")
    val title: Title,

    @SerializedName("Town")
    val town: Town,

    @SerializedName("Tribe")
    val tribe: Tribe?
)

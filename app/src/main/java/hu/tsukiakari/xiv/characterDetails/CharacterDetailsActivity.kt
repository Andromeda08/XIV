package hu.tsukiakari.xiv.characterDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.gson.Gson
import hu.tsukiakari.xiv.characterDetails.adapter.CharacterJobsAdapter
import hu.tsukiakari.xiv.characterDetails.model.JobExtended
import hu.tsukiakari.xiv.databinding.ActivityCharacterDetailsBinding
import hu.tsukiakari.xiv.network.model.lodestone.LodestoneCharacter
import hu.tsukiakari.xiv.network.model.lodestone.LodestoneResponse
import hu.tsukiakari.xiv.utility.matchJobToResource

class CharacterDetailsActivity : AppCompatActivity(), CharacterDataHolder {
    companion object {
        const val CHARACTER_JSON = "lodestoneJsonData"
    }

    private lateinit var binding: ActivityCharacterDetailsBinding
    private var lodestoneJsonData: String? = null
    private var data: LodestoneResponse? = null

    private lateinit var jobsAdapter: CharacterJobsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gson = Gson()
        lodestoneJsonData = intent.getStringExtra(CHARACTER_JSON)
        Log.i("Received data", lodestoneJsonData.toString())

        data = gson.fromJson(lodestoneJsonData, LodestoneResponse::class.java)

        supportActionBar?.title = "Character Details"

        // Fill data
        createHeader()
        createDetailsTable()
        createJobsList()
    }

    override fun getCharacterData(): LodestoneCharacter? {
        return data!!.character
    }

    private fun createHeader() {
        binding.detailsName.text = data!!.character.name
        binding.detailsActiveJob.text = "Lv. ${ data!!.character.activeClassJob.level} ${data!!.character.activeClassJob.unlockedState.name}"
        binding.detailsActiveJobIcon.setBackgroundResource(
            matchJobToResource(this, data!!.character.activeClassJob.unlockedState.name)
        )

        Glide.with(binding.root)
            .load(data!!.character.avatar)
            .transition(DrawableTransitionOptions().crossFade())
            .into(binding.detailsAvatar)
    }

    private fun createDetailsTable() {
        val ch = data!!.character
        binding.detailsTableDcServer.text = "${ch.dataCenter} - ${ch.server}"

        val tribe = if (ch.tribe?.Name == null || ch.tribe.Name.isEmpty()) "" else " - ${ch.tribe.Name}"
        binding.detailsTableRaceAndTribe.text = "${ch.race.Name}$tribe"

        binding.detailsTableNameDay.text = ch.nameday

        binding.detailsTableDeityName.text = ch.guardianDeity.Name
        Glide.with(binding.root)
            .load("https://xivapi.com/${ ch.guardianDeity.Icon }")
            .into(binding.detailsTableDeityIcon)

        binding.detailsTableTownName.text = ch.town.Name
        Glide.with(binding.root)
            .load("https://xivapi.com/${ ch.town.Icon }")
            .into(binding.detailsTableTownIcon)

        if (ch.grandCompany.company == null || ch.grandCompany.rank == null) {
            binding.detailsTableRowCompany.visibility = View.GONE
        }
        else {
            binding.detailsTableCompanyName.text = ch.grandCompany.company.Name
            binding.detailsTableCompanyRankName.text = ch.grandCompany.rank.Name
            Glide.with(binding.root)
                .load("https://xivapi.com/${ ch.grandCompany.rank.Icon }")
                .into(binding.detailsTableCompanyRankIcon)
        }
    }

    private fun createJobsList() {
        jobsAdapter = CharacterJobsAdapter(this)
        binding.jobsList.layoutManager = LinearLayoutManager(this)
        binding.jobsList.adapter = jobsAdapter

        runOnUiThread {
            // Create JobExtended list with proper resource ID-s for images
            jobsAdapter.createList(data!!.character.classJobs.map { it -> JobExtended(it, matchJobToResource(this, it.unlockedState.name)) })
        }
    }


}
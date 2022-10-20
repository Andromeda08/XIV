package hu.tsukiakari.xiv.characterDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import hu.tsukiakari.xiv.R
import hu.tsukiakari.xiv.databinding.ActivityCharacterDetailsBinding
import hu.tsukiakari.xiv.network.XIVApi
import hu.tsukiakari.xiv.network.model.lodestone.LodestoneCharacter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterDetailsActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_LODESTONE_ID = "LODESTONE_ID"
    }

    private lateinit var binding: ActivityCharacterDetailsBinding
    private var character: LodestoneCharacter? = null
    private var lodestone_id: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lodestone_id = intent.getLongExtra("LODESTONE_ID", 0)

        fetchLodestoneData()

        //supportActionBar?.title = "$lodestone_id"
    }

    private fun displayLodestoneData(lodestoneCharacter: LodestoneCharacter?) {
        character = lodestoneCharacter
    }

    private fun fetchLodestoneData() {
        var lodestoneResult: LodestoneCharacter? = null

        XIVApi.getLodestone(lodestone_id!!)?.enqueue(object : Callback<LodestoneCharacter?> {
            override fun onResponse(call: Call<LodestoneCharacter?>, res: Response<LodestoneCharacter?>) {
                if (res.isSuccessful) {
                    displayLodestoneData(res.body())
                }
                else {
                    Snackbar.make(binding.root, "Error: Character not found!", Snackbar.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<LodestoneCharacter?>, t: Throwable) {
                Snackbar.make(binding.root, R.string.lodestone_request_failed, Snackbar.LENGTH_LONG).show()
            }
        })
    }
}
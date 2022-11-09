package hu.tsukiakari.xiv

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import hu.tsukiakari.xiv.characterList.adapter.SavedCharacterAdapter
import hu.tsukiakari.xiv.characterDetails.CharacterDetailsActivity
import hu.tsukiakari.xiv.characterList.AddCharacterDialogFragment
import hu.tsukiakari.xiv.characterList.data.SavedCharacter
import hu.tsukiakari.xiv.characterList.data.SavedCharacterDatabase
import hu.tsukiakari.xiv.databinding.ActivityCharacterListBinding
import hu.tsukiakari.xiv.network.XIVApi
import hu.tsukiakari.xiv.network.model.character.CharacterResult
import hu.tsukiakari.xiv.network.model.lodestone.LodestoneResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

class CharacterListActivity
    : AppCompatActivity()
    , SavedCharacterAdapter.OnCharacterSelectedListener
    , AddCharacterDialogFragment.AddCharacterDialogListener
{
    private lateinit var binding: ActivityCharacterListBinding
    private lateinit var database: SavedCharacterDatabase
    private lateinit var adapter: SavedCharacterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCharacterListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = SavedCharacterDatabase.getDatabase(applicationContext)

        supportActionBar?.title = "Your Characters"

        initFab()
        initRecyclerView()
    }

    private fun initFab() {
        binding.addCharacter.setOnClickListener {
            AddCharacterDialogFragment().show(supportFragmentManager, AddCharacterDialogFragment::class.java.simpleName)
        }
    }

    private fun initRecyclerView() {
        binding.characterList.layoutManager = LinearLayoutManager(this)
        adapter = SavedCharacterAdapter(this)
        binding.characterList.adapter = adapter
        loadSavedCharacters()
    }

    /**
     * Loads saved characters from db
     */
    private fun loadSavedCharacters() {
        thread {
            val items = database.savedCharacterDao().getAll()
            runOnUiThread {
                adapter.initializeList(items)
            }
        }
    }

    /**
     * When selecting a character fetch their lodestone data, then change activities
     */
    override fun onCharacterSelected(character: SavedCharacter?) {
        if (character != null) {
            Snackbar.make(binding.root, "Loading character data for ${character.name}...", Snackbar.LENGTH_LONG).show()

            XIVApi.getLodestone(character.lodestoneID)?.enqueue(object : Callback<LodestoneResponse?> {
                override fun onResponse(call: Call<LodestoneResponse?>, res: Response<LodestoneResponse?>) {
                    if (res.isSuccessful) {
                        // At this point we can & should update the last known active job for the saved character
                        val lastActiveJob = res.body()!!.character.activeClassJob
                        updateSavedCharacter(character.lodestoneID, lastActiveJob.unlockedState.name, lastActiveJob.level, res.body()!!.character.avatar)

                        val gson = Gson()
                        val characterDetailsIntent = Intent()
                        characterDetailsIntent.setClass(this@CharacterListActivity, CharacterDetailsActivity::class.java)
                        characterDetailsIntent.putExtra(CharacterDetailsActivity.CHARACTER_JSON, gson.toJson(res.body()))
                        startActivity(characterDetailsIntent)
                    }
                    else {
                        /* If we somehow reach this point something's really bad anyway */
                        Snackbar.make(binding.root, "Error: Character not found!", Snackbar.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<LodestoneResponse?>, t: Throwable) {
                    Snackbar.make(binding.root, R.string.lodestone_request_failed, Snackbar.LENGTH_LONG).show()
                }
            })
        }
    }

    /**
     * Remove character from db and list
     */
    override fun onCharacterDeleted(character: SavedCharacter?) {
        thread {
            if (character != null) {
                database.savedCharacterDao().delete(character)
                runOnUiThread {
                    adapter.removeCharacter(character)
                }
            }
        }
    }

    /**
     * Fetch basic character data, then persist & add to list
     */
    override fun onCharacterAdded(name: String, server: String) {
        var characterResult: CharacterResult? = null

        XIVApi.getCharacter(name, server)?.enqueue(object : Callback<CharacterResult?> {
            override fun onResponse(call: Call<CharacterResult?>, res: Response<CharacterResult?>) {
                if (res.isSuccessful) {
                    characterResult = res.body()

                    if (characterResult!!.Results.isEmpty()) {
                        Snackbar.make(binding.root, "Error: Character $name@$server not found!", Snackbar.LENGTH_LONG).show()
                    }
                    else {
                        val result = characterResult!!.Results[0]
                        val savedCharacter = SavedCharacter(
                            name = result.Name, server = result.Server, lodestoneID = result.ID, avatar = result.Avatar,
                            jobName = "Unknown", jobLevel = 0)

                        thread {
                            database.savedCharacterDao().insert(savedCharacter)

                            runOnUiThread {
                                adapter.addCharacter(savedCharacter)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<CharacterResult?>, t: Throwable) {
                Snackbar.make(binding.root, R.string.add_character_fail_request, Snackbar.LENGTH_LONG).show()
            }
        })
    }

    /**
     * Updates saved character job information and avatar.
     */
    private fun updateSavedCharacter(lodestone_id: Long, job_name: String, job_level: Int, avatar: String) {
        thread {
            val saved = database.savedCharacterDao().getById(lodestone_id)
            Log.i("Query Result", saved.toString())
            saved!!.jobName = job_name
            saved.jobLevel = job_level
            saved.avatar = avatar

            database.savedCharacterDao().update(saved)
        }

        loadSavedCharacters()
    }
}

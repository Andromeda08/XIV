package hu.tsukiakari.xiv

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import hu.tsukiakari.xiv.adapter.SavedCharacterAdapter
import hu.tsukiakari.xiv.characterDetails.CharacterDetailsActivity
import hu.tsukiakari.xiv.data.SavedCharacter
import hu.tsukiakari.xiv.data.SavedCharacterDatabase
import hu.tsukiakari.xiv.databinding.ActivityCharacterListBinding
import hu.tsukiakari.xiv.network.XIVApi
import hu.tsukiakari.xiv.network.model.character.CharacterResult
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

    private fun loadSavedCharacters() {
        thread {
            val items = database.savedCharacterDao().getAll()
            runOnUiThread {
                adapter.initializeList(items)
            }
        }
    }

    override fun onCharacterSelected(character: SavedCharacter?) {
        if (character != null) {
            val characterDetailsIntent = Intent()
            characterDetailsIntent.setClass(this@CharacterListActivity, CharacterDetailsActivity::class.java)
            characterDetailsIntent.putExtra(CharacterDetailsActivity.EXTRA_LODESTONE_ID, character.lodestoneID)
            startActivity(characterDetailsIntent)
        }
    }

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
}

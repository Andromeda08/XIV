package hu.tsukiakari.xiv.data

import androidx.room.*

@Dao
interface SavedCharacterDAO {
    @Query("SELECT * FROM SavedCharacters")
    fun getAll(): List<SavedCharacter>

    @Query("select * from SavedCharacters where lodestone_id = :id")
    fun getById(id: Long): SavedCharacter?

    @Insert
    fun insert(character: SavedCharacter): Long

    @Update
    fun update(character: SavedCharacter)

    @Delete
    fun delete(character: SavedCharacter)
}
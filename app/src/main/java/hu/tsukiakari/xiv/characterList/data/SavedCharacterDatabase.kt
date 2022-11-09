package hu.tsukiakari.xiv.characterList.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SavedCharacter::class], version = 2, exportSchema = false)
abstract class SavedCharacterDatabase : RoomDatabase() {
    abstract fun savedCharacterDao(): SavedCharacterDAO
    companion object {
        fun getDatabase(ctx: Context): SavedCharacterDatabase {
            return Room.databaseBuilder(
                ctx,
                SavedCharacterDatabase::class.java,
                "SavedCharacters"
            ).build()
        }
    }
}

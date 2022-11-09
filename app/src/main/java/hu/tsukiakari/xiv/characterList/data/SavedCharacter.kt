package hu.tsukiakari.xiv.characterList.data

import android.text.Editable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SavedCharacters")
data class SavedCharacter(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var ID: Long? = null,

    @ColumnInfo(name = "lodestone_id")
    var lodestoneID: Long,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "server")
    var server: String,

    @ColumnInfo(name = "avatar_url")
    var avatar: String,

    @ColumnInfo(name = "last_active_job_name")
    var jobName: String,

    @ColumnInfo(name = "last_active_job_level")
    var jobLevel: Int,
)
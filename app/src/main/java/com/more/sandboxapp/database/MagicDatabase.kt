package com.more.sandboxapp.database

import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import com.more.sandboxapp.ui.theme.mtgForestSecondary
import com.more.sandboxapp.ui.theme.mtgIslandSecondary
import com.more.sandboxapp.ui.theme.mtgMountainSecondary
import com.more.sandboxapp.ui.theme.mtgPlainSecondary
import com.more.sandboxapp.ui.theme.mtgSwampSecondary
import kotlinx.parcelize.Parcelize

@Database(entities = [MagicCard::class, MagicSet::class], version = 3)
abstract class MagicDatabase : RoomDatabase() {
    abstract fun magicCardDao() : MagicCardDao
    abstract fun magicSetDao() : MagicSetDao
}

@Entity
data class MagicCard(
    @PrimaryKey val id: String,
    val name: String,
    val typeLine: String?,
    val blurb: String?,
    val colors: String,
    val smallImage: String?,
    val normalImage: String?,
    val largeImage: String?,
    val pngImage: String?,
    val borderIamge: String?,
    val artCropImage: String?,
    val setId: String,
    val flavorText: String?,
    val oracleText: String?,
    val manaCost: String?
) {
    fun getPrimaryColor(): Color {
        return colors.split(",")[0].toColor()
    }

    fun getAllColors(): List<Color> {
        return colors.split(",").map { it.toColor() }
    }

    fun String.toColor() = when(this) {
        "W" -> mtgPlainSecondary
        "B" -> mtgSwampSecondary
        "R" -> mtgMountainSecondary
        "G" -> mtgForestSecondary
        "U" -> mtgIslandSecondary
        else -> Color.White
    }
}

@Entity
@Parcelize
data class MagicSet(
    @PrimaryKey val id: String,
    val name: String,
    val type: String,
    val code: String,
    val iconUrl: String,
    val releasedDate: String,
    val searchUri: String
) : Parcelable

@Dao
interface MagicCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cards: List<MagicCard>)

    @Update
    suspend fun updateCards(cards: List<MagicCard>)

    @Delete
    suspend fun delete(card: MagicCard)

    @Query("SELECT * FROM MagicCard")
    suspend fun getAll(): List<MagicCard>

    @Query(value = "SELECT * FROM MagicCard where id = :id")
    suspend fun getMagicCard(id: String): MagicCard

    @Query(value = "SELECT * FROM MagicCard where setId = :setId")
    suspend fun getCardsInSet(setId: String): List<MagicCard>
}

@Dao
interface MagicSetDao {
    @Query("SELECT * FROM MagicSet")
    suspend fun getAllSets(): List<MagicSet>

    @Query("SELECT * FROM MagicSet where id = :id")
    suspend fun getSetForId(id: String): MagicSet


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(setList: List<MagicSet>)
}
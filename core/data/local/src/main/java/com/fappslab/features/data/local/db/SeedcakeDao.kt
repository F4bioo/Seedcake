package com.fappslab.features.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fappslab.features.data.local.model.SeedEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface SeedcakeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setSeedPhrase(seed: SeedEntity)

    @Query("DELETE FROM seed WHERE _id = :id")
    suspend fun deleteSeedPhrase(id: Int)

    @Query("SELECT * FROM seed")
    fun getSeedPhrases(): Flow<List<SeedEntity>>
}

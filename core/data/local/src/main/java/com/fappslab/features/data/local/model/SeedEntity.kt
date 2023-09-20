package com.fappslab.features.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "seed")
internal data class SeedEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val id: Int = 0,
    @ColumnInfo(name = "alias")
    val alias: String,
    @ColumnInfo(name = "date_time")
    val date: String,
    @ColumnInfo(name = "encrypted_seed")
    val encryptedSeed: String
)

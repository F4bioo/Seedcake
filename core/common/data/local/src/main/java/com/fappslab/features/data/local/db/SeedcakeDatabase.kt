package com.fappslab.features.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fappslab.features.data.local.model.SeedEntity

@Database(entities = [SeedEntity::class], version = 1, exportSchema = false)
internal abstract class SeedcakeDatabase : RoomDatabase() {
    abstract fun seedcakeDao(): SeedcakeDao
}

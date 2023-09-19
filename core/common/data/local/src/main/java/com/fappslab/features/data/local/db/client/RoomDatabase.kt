package com.fappslab.features.data.local.db.client

import android.content.Context
import androidx.room.Room
import com.fappslab.features.data.local.db.SeedcakeDatabase
import com.fappslab.seedcake.libraries.arch.source.Source

internal class RoomDatabase(
    private val context: Context,
    private val name: String
) : Source<SeedcakeDatabase> {

    override fun create(): SeedcakeDatabase {
        return Room.databaseBuilder(
            context,
            SeedcakeDatabase::class.java,
            name
        ).build()
    }
}

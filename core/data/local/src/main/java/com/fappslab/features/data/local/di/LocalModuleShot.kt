package com.fappslab.features.data.local.di

import android.content.SharedPreferences
import com.fappslab.features.common.domain.repository.ObfuscationRepository
import com.fappslab.features.common.domain.repository.StorageRepository
import com.fappslab.features.data.local.db.SeedcakeDao
import com.fappslab.features.data.local.db.client.RoomDatabase
import com.fappslab.features.data.local.db.client.SecurePreferences
import com.fappslab.features.data.local.db.client.SimplePreferences
import com.fappslab.features.data.local.model.SourceProvider
import com.fappslab.features.data.local.repository.ObfuscationRepositoryImpl
import com.fappslab.features.data.local.repository.StorageRepositoryImpl
import com.fappslab.features.data.local.source.SeedcakeDataSource
import com.fappslab.features.data.local.source.SeedcakeDataSourceImpl
import com.fappslab.seedcake.core.data.local.BuildConfig
import com.fappslab.seedcake.libraries.arch.koin.KoinQualifier
import com.fappslab.seedcake.libraries.arch.koin.koinshot.KoinShot
import org.koin.core.module.Module
import org.koin.core.scope.Scope
import org.koin.dsl.module

object SecurePrefsQualifier : KoinQualifier
object SimplePrefsQualifier : KoinQualifier

internal class LocalModuleShot : KoinShot() {

    override val dataModule: Module = module {
        single<SeedcakeDao> {
            RoomDatabase(context = get(), name = BuildConfig.DB_NAME).create().seedcakeDao()
        }
        single<SharedPreferences>(qualifier = SecurePrefsQualifier) {
            SecurePreferences(context = get(), name = BuildConfig.SECURE_PREFS_FILE_NAME).create()
        }
        single<SharedPreferences>(SimplePrefsQualifier) {
            SimplePreferences(context = get(), name = BuildConfig.SIMPLE_PREFS_FILE_NAME).create()
        }
        factory<ObfuscationRepository> {
            ObfuscationRepositoryImpl(dataSource = getSeedDataSource())
        }
        factory<StorageRepository> {
            StorageRepositoryImpl(dataSource = getSeedDataSource())
        }
    }

    private fun Scope.getSeedDataSource(): SeedcakeDataSource =
        SeedcakeDataSourceImpl(
            provider = SourceProvider(
                cryptoManager = get(),
                bip39Words = get(),
                bIP39Colors = get(),
                dao = get(),
                securePreferences = get(SecurePrefsQualifier),
                simplePreferences = get(SimplePrefsQualifier)
            )
        )
}

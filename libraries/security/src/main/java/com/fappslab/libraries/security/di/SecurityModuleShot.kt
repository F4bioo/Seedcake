package com.fappslab.libraries.security.di

import com.fappslab.libraries.security.bip39colors.BIP39Colors
import com.fappslab.libraries.security.bip39colors.BIP39ColorsImpl
import com.fappslab.libraries.security.bip39words.Bip39Words
import com.fappslab.libraries.security.bip39words.Bip39WordsImpl
import com.fappslab.libraries.security.cryptography.CryptoManager
import com.fappslab.libraries.security.cryptography.CryptoManagerImpl
import com.fappslab.seedcake.libraries.arch.koin.koinshot.KoinShot
import org.koin.core.module.Module
import org.koin.dsl.module

internal object SecurityModuleShot : KoinShot() {

    override val dataModule: Module = module {
        single<Bip39Words> {
            Bip39WordsImpl(context = get())
        }

        factory<BIP39Colors> {
            BIP39ColorsImpl(wordList = get<Bip39Words>().wordList())
        }

        factory<CryptoManager> {
            CryptoManagerImpl(wordList = get<Bip39Words>().wordList())
        }
    }
}

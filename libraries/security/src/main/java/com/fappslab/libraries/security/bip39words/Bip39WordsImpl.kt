package com.fappslab.libraries.security.bip39words

import android.content.Context
import com.fappslab.seedcake.libraries.security.R

class Bip39WordsImpl(private val context: Context) : Bip39Words {

    override fun wordList(): List<String> {
        val inputStream = context.resources.openRawResource(R.raw.bip39_word_list)
        return inputStream.bufferedReader().useLines { it.toList() }
    }
}

package com.fappslab.features.common.domain.model

data class Seed(
    val id: Int = 0,
    val alias: String,
    val date: String,
    val unreadableSeedPhrase: String
)

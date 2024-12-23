package com.nsicyber.mojimapper.presentation

import com.nsicyber.mojimapper.domain.model.EmojiMapModel

data class MainState(
    val mapObject: List<EmojiMapModel> = listOf(),
    val lastLoadedLongitude:Double=0.0,
    val lastLoadedLatitude:Double=0.0,
    val onSuccess:Boolean=false,
)
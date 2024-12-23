package com.nsicyber.mojimapper.domain.mapFunctions

import com.nsicyber.mojimapper.data.getEmoji
import com.nsicyber.mojimapper.data.model.EmojiData
import com.nsicyber.mojimapper.domain.model.EmojiMapModel
import java.util.UUID

fun EmojiData?.toDomainModel(): EmojiMapModel {
    return EmojiMapModel(
        id = this?.id ?: UUID.randomUUID().toString(),
        emoji = this?.emoji.getEmoji() ?: ":D", longitude = this?.longitude?:0.0, latitude = this?.latitude?:0.0
    )
}
package com.more.sandboxapp.api.magic.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CardResponse(val cards: List<Card>)

@JsonClass(generateAdapter = true)
data class Card(val name: String,
                val manaCost: String?,
                val cmc: String,
                val text: String?,
                val id: String,
                val colors: List<String>?,
                @ImageUrl val imageUrl: String?,
                val foreignNames: List<ForeignName>?,
                val type: String) {
}

@JsonClass(generateAdapter = true)
data class ForeignName(val name: String,
                       val text: String?,
                       val language: String)

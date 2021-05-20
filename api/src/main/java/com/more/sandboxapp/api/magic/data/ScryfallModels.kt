package com.more.sandboxapp.api.magic.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonQualifier

@JsonClass(generateAdapter = true)
data class SetsResponse(
    @Json(name = "has_more") val hasMore: Boolean,
    val data: List<CardSet>
)


@JsonClass(generateAdapter = true)
data class CardSet(
    val id: String,
    val code: String,
    @Json(name = "set_type") val type: String,
    val name: String,
    val uri: String,
    @Json(name = "scryfall_uri") val scryfallUri: String,
    @Json(name = "released_at") val releasedAt: String,
    @Json(name = "icon_svg_uri") val icon_svg_uri: String,
    @Json(name = "search_uri") val searchUri: String)


@JsonClass(generateAdapter = true)
data class CardSearchResponse(
    @Json(name = "has_more") val hasMore: Boolean,
    @Json(name = "total_cards") val totalCards: Int,
    @Json(name = "next_page") val nextPageUrl: String?,
    val data: List<MagicCard>)

@JsonClass(generateAdapter = true)
data class MagicCard(
    val id: String,
    val name: String,
    @Json(name = "type_line") val typeLine: String,
    @Json(name = "oracle_text") val oracleText: String?,
    @Json(name = "image_uris") val cardImages: CardImages?,
    @Json(name = "flavor_text") val flavorText: String?,
    val colors: List<String>?,
    @Json(name = "mana_cost") val manaCost: String?,
    @Json(name ="set_uri") val setUri: String?)

@JsonClass(generateAdapter = true)
data class CardImages(val small: String?,
                      val normal: String?,
                      val large: String?,
                      val png: String?,
                      @Json(name="art_crop") val artCrop: String?,
                      @Json(name="border_crop") val borderCrop: String?)
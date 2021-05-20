package com.more.sandboxapp.models

import androidx.compose.foundation.text.InlineTextContent
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SymbolList(
    val data: List<Symbol>
)

@JsonClass(generateAdapter = true)
data class Symbol(
    val symbol: String,
    val svg_uri: String
)

fun SymbolList.toSymbolMap(): Map<String, Symbol> {
    val symbolMap = mutableMapOf<String, Symbol>()
    this.data.forEach {
        symbolMap[it.symbol] = it
    }
    return symbolMap
}

fun Map<String, Symbol>.toInlineTextContentMap(builder: (Symbol) -> InlineTextContent) = this.mapValues { entry ->
    builder(entry.value)
}
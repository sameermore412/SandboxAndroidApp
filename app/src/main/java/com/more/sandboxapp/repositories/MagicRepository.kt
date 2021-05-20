package com.more.sandboxapp.repositories

import com.more.sandboxapp.api.magic.MagicService
import com.more.sandboxapp.api.magic.ScryfallService
import com.more.sandboxapp.database.MagicCard
import com.more.sandboxapp.database.MagicDatabase
import com.more.sandboxapp.database.MagicSet
import com.more.sandboxapp.models.Symbol
import com.more.sandboxapp.models.SymbolList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Named

class MagicRepository @Inject constructor(
    private val magicService: MagicService,
    private val scryfallService: ScryfallService,
    @Named("MagicDatabase") private val magicDatabase: MagicDatabase) {

    fun getCardsInSet(setId:String): Flow<List<MagicCard>> = flow {
        var storedCards = magicDatabase.magicCardDao().getCardsInSet(setId)
        if (storedCards.isEmpty()) {
            val magicSet = magicDatabase.magicSetDao().getSetForId(setId)
            storedCards = getAllCards(magicSet.searchUri, setId, mutableListOf())
            magicDatabase.magicCardDao().insertAll(storedCards)
        }
        emit(storedCards)
    }

    private suspend fun getAllCards(setUrl: String?, setId: String, list: MutableList<MagicCard>): List<MagicCard> {
        if (setUrl == null) {
            return list
        } else {
            val response = scryfallService.getCardsFromUrl(setUrl)
            val cards = response.data.map { networkCard ->
                val cardSetId = networkCard.setUri?.split("/")?.last() ?: setId
                MagicCard(
                    id = networkCard.id,
                    name = networkCard.name,
                    typeLine = networkCard.typeLine,
                    blurb = networkCard.oracleText,
                    colors = networkCard.colors?.joinToString(",") ?: "",
                    normalImage = networkCard.cardImages?.normal ?: "",
                    smallImage = networkCard.cardImages?.small ?: "",
                    largeImage = networkCard.cardImages?.large ?: "",
                    pngImage = networkCard.cardImages?.png ?: "",
                    borderIamge = networkCard.cardImages?.borderCrop ?: "",
                    artCropImage = networkCard.cardImages?.artCrop ?: "",
                    oracleText = networkCard.oracleText,
                    flavorText = networkCard.flavorText,
                    setId = cardSetId,
                    manaCost = networkCard.manaCost
                )
            }
            list.addAll(cards)
            delay(100)
            return getAllCards(response.nextPageUrl, setId, list)
        }
    }


    fun getScryFallSets(): Flow<List<MagicSet>> = flow {
        var setList = magicDatabase.magicSetDao().getAllSets()

        if (setList.isEmpty()) {
            val sets = scryfallService.getSets()
            setList = sets.data.map { cardSet -> MagicSet(
                cardSet.id,
                cardSet.name,
                cardSet.type,
                cardSet.code,
                cardSet.icon_svg_uri,
                cardSet.releasedAt,
                cardSet.searchUri)
            }

            magicDatabase.magicSetDao().insertAll(setList)
        }
        emit(setList)
    }.flowOn(Dispatchers.IO).catch { emit(listOf()) }

    fun getCardDetails(id: String): Flow<MagicCard?> = flow<MagicCard?> {
        var card = magicDatabase.magicCardDao().getMagicCard(id)
        emit(card)
    }.flowOn(Dispatchers.IO).catch { emit(null) }


}
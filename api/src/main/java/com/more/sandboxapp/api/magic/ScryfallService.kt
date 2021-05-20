package com.more.sandboxapp.api.magic

import com.more.sandboxapp.api.magic.data.CardSearchResponse
import com.more.sandboxapp.api.magic.data.CardSet
import com.more.sandboxapp.api.magic.data.SetsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface ScryfallService {

    @GET("sets")
    suspend fun getSets(): SetsResponse

    @GET("sets/{id}")
    suspend fun getSetForId(@Path("id") id: String): CardSet

    @GET
    suspend fun getCardsFromUrl(@Url url: String): CardSearchResponse
}
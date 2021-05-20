package com.more.sandboxapp.api.magic

import com.more.sandboxapp.api.magic.data.CardResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MagicService {

    @GET("v1/cards?subtypes=Dragon&rarity=Mythic")
    suspend fun getCards(@Query("page") page: Int = 0,
                         @Query("pageSize") pageSize: Int = 100) : CardResponse

    @GET("v1/sets/{setName}/booster")
    suspend fun getBooster(@Path("setName") setName:String) : CardResponse
}
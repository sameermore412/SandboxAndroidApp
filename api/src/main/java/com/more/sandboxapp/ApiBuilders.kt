package com.more.sandboxapp

import com.more.sandboxapp.api.magic.MagicService
import com.more.sandboxapp.api.magic.ScryfallService
import com.more.sandboxapp.api.magic.data.ImageUrlAdapter
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiBuilders {
    fun MagicServiceBuilder(okHttpClient: OkHttpClient = OkHttpClient()) : MagicService {
        val moshi = Moshi.Builder()
            .add(ImageUrlAdapter())
            .build()

        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("https://api.magicthegathering.io/")
            .client(okHttpClient)
            .build()
            .create(MagicService::class.java)
    }

    fun ScryfallServiceBuilder(okHttpClient: OkHttpClient = OkHttpClient()) : ScryfallService {
        val moshi = Moshi.Builder()
            .build()

        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("https://api.scryfall.com/")
            .client(okHttpClient)
            .build()
            .create(ScryfallService::class.java)
    }
}
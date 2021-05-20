package com.more.sandboxapp.api.magic.data

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import com.squareup.moshi.JsonQualifier


@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class ImageUrl

class ImageUrlAdapter {
    @ToJson
    fun toJson(@ImageUrl imageUrl: String) = imageUrl

    @FromJson
    @ImageUrl
    fun fromJson(imageUrl: String) = imageUrl.replace("http://", "https://")

}
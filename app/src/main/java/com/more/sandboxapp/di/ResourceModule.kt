package com.more.sandboxapp.di

import android.content.Context
import com.more.sandboxapp.R
import com.more.sandboxapp.models.Symbol
import com.more.sandboxapp.models.SymbolList
import com.more.sandboxapp.models.toSymbolMap
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ResourceModule {

    @Provides
    fun provideMoshi() = Moshi.Builder().build()

    @Provides
    fun provideSymbols(@ApplicationContext appContext: Context, moshi: Moshi): Map<String, Symbol> {
        return appContext.resources.openRawResource(R.raw.symbols)
            .bufferedReader().use {
                moshi.adapter(SymbolList::class.java).fromJson(it.readText())?.toSymbolMap()
            } ?: mutableMapOf()
    }

}
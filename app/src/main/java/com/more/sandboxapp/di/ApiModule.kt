package com.more.sandboxapp.di

import android.content.Context
import androidx.room.Room
import com.more.sandboxapp.ApiBuilders
import com.more.sandboxapp.database.MagicDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    fun provideOkHttpClient() = OkHttpClient
        .Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
        .build()

    @Provides
    fun provideMagicService(okHttpClient: OkHttpClient) =
        ApiBuilders.MagicServiceBuilder(okHttpClient)

    @Provides
    fun provideScryfallService(okHttpClient: OkHttpClient) =
        ApiBuilders.ScryfallServiceBuilder(okHttpClient)

    @Provides
    @Named("MagicDatabase")
    fun provideMagicDataBase(@ApplicationContext context : Context): MagicDatabase {
        return Room.databaseBuilder(context.applicationContext, MagicDatabase::class.java,
            "magic.db"
        ).fallbackToDestructiveMigration().build()
    }
}
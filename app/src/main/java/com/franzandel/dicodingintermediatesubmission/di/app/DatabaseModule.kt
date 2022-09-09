package com.franzandel.dicodingintermediatesubmission.di.app

import android.content.Context
import androidx.room.Room
import com.franzandel.dicodingintermediatesubmission.data.database.StoriesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Franz Andel
 * on 29 May 2022.
 */

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    private const val STORIES_DB = "stories-db"

    @Provides
    @Singleton
    fun provideStoriesDatabase(@ApplicationContext context: Context): StoriesDatabase =
        Room.databaseBuilder(
            context,
            StoriesDatabase::class.java,
            STORIES_DB
        ).build()
}

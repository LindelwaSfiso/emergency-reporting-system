package org.xhanka.ndm_project.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.xhanka.ndm_project.data.database.MainDataBase
import javax.inject.Singleton

/**
 * Dependency Injection App Module
 */

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val DATABASE_NAME = "main_database.db"

    @Singleton
    @Provides
    fun providesMainDataBase(
        @ApplicationContext context: Context
    ): MainDataBase {
        return Room.databaseBuilder(
            context.applicationContext,
            MainDataBase::class.java, DATABASE_NAME
        ).fallbackToDestructiveMigration().build()

        // .fallbackToDestructiveMigration() means
        // wipe and rebuild instead of migrating if no Migration object.
    }
}
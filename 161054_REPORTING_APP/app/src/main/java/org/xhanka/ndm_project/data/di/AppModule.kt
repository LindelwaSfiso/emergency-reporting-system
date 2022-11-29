package org.xhanka.ndm_project.data.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.xhanka.ndm_project.R
import org.xhanka.ndm_project.data.api.WeatherApiSource
import org.xhanka.ndm_project.data.api.WeatherApiService
import org.xhanka.ndm_project.data.database.MainDataBase
import org.xhanka.ndm_project.utils.SendSmsService
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


    @Singleton
    @Provides
    fun provideNewsApiService(
        apiClientSource: WeatherApiSource,
    ): WeatherApiService {
        return apiClientSource.getNewsApiService()
    }
}
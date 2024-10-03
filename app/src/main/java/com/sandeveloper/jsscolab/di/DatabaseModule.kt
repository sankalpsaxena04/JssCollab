package com.sandeveloper.jsscolab.di

import android.content.Context
import androidx.room.Room
import com.sandeveloper.jsscolab.data.Room.AppsDatabase
import com.sandeveloper.jsscolab.data.Room.MessageDAO
import com.sandeveloper.jsscolab.data.Room.MessageDatabase
import com.sandeveloper.jsscolab.data.Room.NotificationDatabase
import com.sandeveloper.jsscolab.data.Room.SwapDAO
import com.sandeveloper.jsscolab.data.Room.SwapDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun providesMessageDB(@ApplicationContext context: Context):MessageDatabase{
        return Room.databaseBuilder(context,MessageDatabase::class.java,"MessageDB").build()
    }
    @Singleton
    @Provides
    fun providesAppsDB(@ApplicationContext context: Context):AppsDatabase{
        return Room.databaseBuilder(context,AppsDatabase::class.java,"AppsDB").build()
    }

    @Singleton
    @Provides
    fun provideNotificationDB(@ApplicationContext context: Context): NotificationDatabase {
        return Room.databaseBuilder(context,NotificationDatabase::class.java,"NotificationDB").build()
    }


    @Provides
    fun provideMessageDao(database: MessageDatabase): MessageDAO {
        return database.messageDAO()
    }

    @Singleton
    @Provides
    fun providesSwapDB(@ApplicationContext context: Context):SwapDatabase{
        return Room.databaseBuilder(context,SwapDatabase::class.java,"SwapDB").build()
    }
    @Provides
    fun provideSwapItem(database: SwapDatabase): SwapDAO {
        return database.swapDao()
    }
}
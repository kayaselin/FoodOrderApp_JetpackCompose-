package com.example.foodorderapp.di

import android.content.Context
import androidx.room.Room
import com.example.foodorderapp.data.datasource.FavorilerDatabase
import com.example.foodorderapp.data.datasource.FoodsDataSource
import com.example.foodorderapp.data.datasource.RemoteFoodsDataSource
import com.example.foodorderapp.data.repository.FavorilerRepository
import com.example.foodorderapp.data.repository.FoodsRepository
import com.example.foodorderapp.retrofit.FavorilerDao
import com.example.foodorderapp.retrofit.FoodsDao
import com.example.foodorderapp.retrofit.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFoodsDao(): FoodsDao {
        return RetrofitClient.instance
    }

    @Provides
    @Singleton
    fun provideFoodsDataSource(foodsDao: FoodsDao): FoodsDataSource {
        return RemoteFoodsDataSource(foodsDao)
    }

    @Provides
    @Singleton
    fun provideFoodsRepository(dataSource: FoodsDataSource): FoodsRepository {
        return FoodsRepository(dataSource)
    }


    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FavorilerDatabase {
        return Room.databaseBuilder(
            context,
            FavorilerDatabase::class.java,
            "favoriler_db"
        ).fallbackToDestructiveMigration()
            .build()
    }


    @Provides
    @Singleton
    fun provideFavorilerDao(db: FavorilerDatabase): FavorilerDao {
        return db.favorilerDao()
    }


    @Provides
    @Singleton
    fun provideFavorilerRepository(favorilerDao: FavorilerDao): FavorilerRepository {
        return FavorilerRepository(favorilerDao)
    }
}

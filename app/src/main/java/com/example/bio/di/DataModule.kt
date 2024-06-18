package com.example.bio.di

import com.example.bio.data.CatalogRemoteDataSourceImpl
import com.example.bio.data.RepositoryImpl
import com.example.bio.domain.repository.CatalogRemoteDataSource
import com.example.bio.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideRepository(catalogRemoteDataSource: CatalogRemoteDataSource): Repository {
        return RepositoryImpl(catalogRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideCatalogRemoteDataSource(): CatalogRemoteDataSource {
        return CatalogRemoteDataSourceImpl()
    }

}
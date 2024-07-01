package com.example.bio.di

import com.example.bio.data.datasource.CatalogRemoteDataSourceImpl
import com.example.bio.data.datasource.ConditionRemoteDataSourceImpl
import com.example.bio.data.repository.RepositoryImpl
import com.example.bio.data.datasource.FilterRemoteDataSourceImpl
import com.example.bio.data.repository.RepositoryConditionImpl
import com.example.bio.domain.repository.CatalogRemoteDataSource
import com.example.bio.domain.repository.ConditionRemoteDataSource
import com.example.bio.domain.repository.FilterRemoteDataSource
import com.example.bio.domain.repository.Repository
import com.example.bio.domain.repository.RepositoryCondition
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
    fun provideRepository(
        catalogRemoteDataSource: CatalogRemoteDataSource,
        searchRemoteDataSource: FilterRemoteDataSource
    ): Repository {
        return RepositoryImpl(catalogRemoteDataSource, searchRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideRepositoryCondition(
        conditionRemoteDataSource: ConditionRemoteDataSource
    ): RepositoryCondition {
        return RepositoryConditionImpl(conditionRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideCatalogRemoteDataSource(): CatalogRemoteDataSource {
        return CatalogRemoteDataSourceImpl()
    }

    @Provides
    @Singleton
    fun provideFilterRemoteDataSource(): FilterRemoteDataSource {
        return FilterRemoteDataSourceImpl()
    }

    @Provides
    @Singleton
    fun provideConditionRemoteDataSource(): ConditionRemoteDataSource {
        return ConditionRemoteDataSourceImpl()
    }

}
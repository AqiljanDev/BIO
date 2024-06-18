package com.example.bio.di

import com.example.bio.domain.repository.Repository
import com.example.bio.domain.useCase.GetCatalogUseCase
import com.example.bio.domain.useCase.GetCollectCharactersUseCase
import com.example.bio.domain.useCase.GetSearchResultsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideGetCatalogUseCase(repository: Repository): GetCatalogUseCase {
        return GetCatalogUseCase(repository)
    }

    @Provides
    fun provideGetCollectCharactersUseCase(repository: Repository): GetCollectCharactersUseCase {
        return GetCollectCharactersUseCase(repository)
    }

    @Provides
    fun provideGetSearchResultsUseCase(repository: Repository): GetSearchResultsUseCase {
        return GetSearchResultsUseCase(repository)
    }
}
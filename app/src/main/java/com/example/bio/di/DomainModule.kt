package com.example.bio.di

import com.example.bio.domain.repository.Repository
import com.example.bio.domain.repository.RepositoryCondition
import com.example.bio.domain.useCase.DeleteCartUseCase
import com.example.bio.domain.useCase.GetCartFullUseCase
import com.example.bio.domain.useCase.GetCatalogFilterUseCase
import com.example.bio.domain.useCase.GetCatalogUseCase
import com.example.bio.domain.useCase.GetCategoriesListUseCase
import com.example.bio.domain.useCase.GetCollectCharactersUseCase
import com.example.bio.domain.useCase.GetCompareFullUseCase
import com.example.bio.domain.useCase.GetMiniCartUseCase
import com.example.bio.domain.useCase.GetMiniCompareUseCase
import com.example.bio.domain.useCase.GetMiniWishListGetUseCase
import com.example.bio.domain.useCase.GetOrdersFindMyUseCase
import com.example.bio.domain.useCase.GetOrdersFindOneUseCase
import com.example.bio.domain.useCase.GetProductCardUseCase
import com.example.bio.domain.useCase.GetProfileDiscountUseCase
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
    fun provideGetCategoriesListUseCase(repository: Repository): GetCategoriesListUseCase {
        return GetCategoriesListUseCase(repository)
    }

    @Provides
    fun provideGetCollectCharactersUseCase(repository: Repository): GetCollectCharactersUseCase {
        return GetCollectCharactersUseCase(repository)
    }

    @Provides
    fun provideGetSearchResultsUseCase(repository: Repository): GetSearchResultsUseCase {
        return GetSearchResultsUseCase(repository)
    }

    @Provides
    fun provideGetProductCardUseCase(repository: Repository): GetProductCardUseCase {
        return GetProductCardUseCase(repository)
    }

    @Provides
    fun provideGetMiniWishListGetUseCase(repositoryCondition: RepositoryCondition): GetMiniWishListGetUseCase {
        return GetMiniWishListGetUseCase(repositoryCondition)
    }

    @Provides
    fun provideGetMiniCompareUseCase(repositoryCondition: RepositoryCondition): GetMiniCompareUseCase {
        return GetMiniCompareUseCase(repositoryCondition)
    }

    @Provides
    fun provideGetCompareFullUseCase(repositoryCondition: RepositoryCondition): GetCompareFullUseCase {
        return GetCompareFullUseCase(repositoryCondition)
    }

    @Provides
    fun provideGetMiniCartUseCase(repositoryCondition: RepositoryCondition): GetMiniCartUseCase {
        return GetMiniCartUseCase(repositoryCondition)
    }

    @Provides
    fun provideDeleteCartUseCase(repositoryCondition: RepositoryCondition): DeleteCartUseCase {
        return DeleteCartUseCase(repositoryCondition)
    }

    @Provides
    fun provideGetCartFullUseCase(repositoryCondition: RepositoryCondition): GetCartFullUseCase {
        return GetCartFullUseCase(repositoryCondition)
    }

    @Provides
    fun provideGetCatalogFilterUseCase(repository: Repository): GetCatalogFilterUseCase {
        return GetCatalogFilterUseCase(repository)
    }

    @Provides
    fun provideGetProfileDiscountUseCase(repository: Repository): GetProfileDiscountUseCase {
        return GetProfileDiscountUseCase(repository)
    }

    @Provides
    fun provideGetOrdersFindMyUseCase(repository: Repository): GetOrdersFindMyUseCase {
        return GetOrdersFindMyUseCase(repository)
    }

    @Provides
    fun provideGetOrdersFindOneUseCase(repository: Repository): GetOrdersFindOneUseCase {
        return GetOrdersFindOneUseCase(repository)
    }

}
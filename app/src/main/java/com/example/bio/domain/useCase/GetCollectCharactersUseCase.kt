package com.example.bio.domain.useCase

import com.example.bio.domain.entities.collectCharacters.CollectCharacter
import com.example.bio.domain.repository.Repository
import javax.inject.Inject

class GetCollectCharactersUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend fun execute(token: String, catalog: String): CollectCharacter {
        return repository.getCollectCharacters(token, catalog)
    }
}
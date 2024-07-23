package com.example.bio.domain.useCase

import com.example.bio.domain.entities.Cabinet
import com.example.bio.domain.repository.Repository
import javax.inject.Inject

class GetCabinetFindMyUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend fun execute(token: String): Cabinet {
        return repository.getCabinetFindMy(token)
    }
}
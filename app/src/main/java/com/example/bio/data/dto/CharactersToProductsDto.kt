package com.example.bio.data.dto

import com.example.bio.domain.entities.findOne.Character
import com.example.bio.domain.entities.findOne.CharacterValue
import com.example.bio.domain.entities.findOne.CharactersToProducts

data class CharactersToProductsDto (
    override val id: Int,
    override val order: Int,
    override val characterId: String,
    override val characterValueId: String,
    override val productId: String,
    override val character: CharacterDto,
    override val characterValue: CharacterValueDto
) : CharactersToProducts

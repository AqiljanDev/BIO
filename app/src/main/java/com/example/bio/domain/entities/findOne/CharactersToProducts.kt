package com.example.bio.domain.entities.findOne

interface CharactersToProducts {
    val id: Int
    val order: Int
    val characterId: String
    val characterValueId: String
    val productId: String
    val character: Character
    val characterValue: CharacterValue
}

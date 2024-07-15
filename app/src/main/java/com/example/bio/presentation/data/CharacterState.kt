package com.example.bio.presentation.data

import com.example.bio.domain.entities.collectCharacters.Character

data class CharacterState(
    val character: Character,
    var isActive: Boolean
)

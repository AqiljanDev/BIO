package com.example.bio.data.dto.collectCharacters

import com.example.bio.domain.entities.collectCharacters.Character

data class CharacterCollectDto (
    override val id: Int,
    override val id1c: String,
    override val title: String,
    override val characterId: String
) : Character

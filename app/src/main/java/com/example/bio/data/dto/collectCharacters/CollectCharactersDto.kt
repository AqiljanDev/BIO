package com.example.bio.data.dto.collectCharacters

import com.example.bio.domain.entities.collectCharacters.CollectCharacter

data class CollectCharactersDto (
    override val pages: Int,
    override val characters: List<BrandDto>
) : CollectCharacter
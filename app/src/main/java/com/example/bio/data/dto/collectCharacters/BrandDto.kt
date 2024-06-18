package com.example.bio.data.dto.collectCharacters

import com.example.bio.data.dto.CharacterDto
import com.example.bio.domain.entities.collectCharacters.Brand
import com.example.bio.domain.entities.collectCharacters.Character

data class BrandDto (
    override val id: Int,
    override val id1c: String,
    override val title: String,
    override val characters: List<CharacterCollectDto>
) : Brand

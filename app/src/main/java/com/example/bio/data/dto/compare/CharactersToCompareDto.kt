package com.example.bio.data.dto.compare

import com.example.bio.domain.entities.compare.CharactersToCompare

data class CharactersToCompareDto(
    override val id: Int,
    override val id1c: String,
    override val title: String
): CharactersToCompare

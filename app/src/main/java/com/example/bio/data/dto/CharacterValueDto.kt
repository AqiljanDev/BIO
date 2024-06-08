package com.example.bio.data.dto

import com.example.bio.domain.entities.findOne.CharacterValue

data class CharacterValueDto (
    override val id: Int,
    override val id1c: String,
    override val title: String,
    override val characterId: String
) : CharacterValue

package com.example.bio.data.dto

import com.example.bio.domain.entities.findOne.Character

data class CharacterDto (
    override val id: Int,
    override val id1c: String,
    override val title: String
) : Character

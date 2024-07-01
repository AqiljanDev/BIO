package com.example.bio.domain.entities.compare

import com.example.bio.domain.entities.findOne.CharactersToProducts

interface CompareFull {
    val characters: List<CharactersToCompare>
    val products: List<ProductWrapper>
}
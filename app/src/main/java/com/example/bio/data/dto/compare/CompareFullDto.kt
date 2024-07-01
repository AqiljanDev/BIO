package com.example.bio.data.dto.compare

import com.example.bio.domain.entities.compare.CharactersToCompare
import com.example.bio.domain.entities.compare.CompareFull
import com.example.bio.domain.entities.compare.ProductWrapper

data class CompareFullDto (
    override val characters: List<CharactersToCompareDto>,
    override val products: List<ProductWrapperDto>
): CompareFull
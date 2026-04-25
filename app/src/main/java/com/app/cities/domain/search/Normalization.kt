package com.app.cities.domain.search

import java.text.Normalizer

fun normalize(text: String): String {
    return Normalizer.normalize(text.lowercase(), Normalizer.Form.NFD)
        .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
}
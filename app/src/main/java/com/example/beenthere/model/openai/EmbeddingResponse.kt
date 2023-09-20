package com.example.beenthere.model.openai

import com.squareup.moshi.Json


data class EmbeddingResponse(
    val data: List<EmbeddingData>,
    val model: String,
    val obj: String,
    val usage: Usage
)

data class EmbeddingData(
    val embedding: List<Double>,
    val index: Int,
    val obj: String
)

data class UsageEmb(
    @Json(name = "prompt_tokens") val promptTokens: Int,
    @Json(name = "total_tokens") val totalTokens: Int
)

// structure
// doesn't follow spec
// bug go back
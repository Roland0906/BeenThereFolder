package com.example.beenthere.model.openai

data class EmbeddingRequest(
    val input: String,
    val model: String
)

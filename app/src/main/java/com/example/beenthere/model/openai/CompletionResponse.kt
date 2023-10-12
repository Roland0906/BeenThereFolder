package com.example.beenthere.model.openai

import com.squareup.moshi.Json

data class CompletionResponse(
    val id: String,
    val obj: String,
    val created: Long,
    val model: String,
    val choices: List<Choice>,
    val usage: Usage
)

data class Choice(
    val text: String,
    val index: Int,
    @Json(name = "logprobs") val logProbs: Any?,
    @Json(name = "finish_reason") val finishReason: String
)

data class Usage(
    @Json(name = "prompt_tokens") val promptTokens: Int,
    @Json(name = "completion_tokens") val completionTokens: Int,
    @Json(name = "total_tokens") val totalTokens: Int
)

package com.example.beenthere.data.openai


import com.example.beenthere.model.openai.CompletionRequest
import com.example.beenthere.model.openai.CompletionResponse
import com.example.beenthere.model.openai.EmbeddingRequest
import com.example.beenthere.model.openai.EmbeddingResponse
import com.example.beenthere.utils.OPEN_AI.OPEN_API_KEY
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAiApi {
    @Headers("Authorization: Bearer $OPEN_API_KEY")
    @POST("v1/completions")
    suspend fun getCompletions(@Body completionRequest: CompletionRequest) : Response<CompletionResponse>


    @Headers("Authorization: Bearer $OPEN_API_KEY")
    @POST("v1/embeddings")
    suspend fun getEmbeddings(@Body embeddingRequest: EmbeddingRequest) : Response<EmbeddingResponse>


}
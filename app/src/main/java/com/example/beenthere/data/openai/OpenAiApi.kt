package com.example.beenthere.data.openai


import android.app.Application
import com.example.beenthere.BeenThereApplication
import com.example.beenthere.model.openai.CompletionRequest
import com.example.beenthere.model.openai.CompletionResponse
import com.example.beenthere.model.openai.EmbeddingRequest
import com.example.beenthere.model.openai.EmbeddingResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import com.example.beenthere.BuildConfig
import com.example.beenthere.utils.Constants

interface OpenAiApi {
    @Headers("Authorization: Bearer ${BuildConfig.OPEN_AI_KEY}")
    @POST("v1/completions")
    suspend fun getCompletions(@Body completionRequest: CompletionRequest) : Response<CompletionResponse>


    @Headers("Authorization: Bearer ${BuildConfig.OPEN_AI_KEY}")
    @POST("v1/embeddings")
    suspend fun getEmbeddings(@Body embeddingRequest: EmbeddingRequest) : Response<EmbeddingResponse>


}
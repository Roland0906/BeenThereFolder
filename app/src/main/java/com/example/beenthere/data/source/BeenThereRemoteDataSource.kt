package com.example.beenthere.data.source

import androidx.lifecycle.LiveData
import com.example.beenthere.model.Books
import retrofit2.Response

object BeenThereRemoteDataSource : BeenThereRepository {

    override suspend fun getBooks(title: String, apiKey: String): Response<Books> {
        return super.getBooks(title, apiKey)
    }


}
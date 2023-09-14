package com.example.beenthere.data.source

import com.example.beenthere.model.Books
import retrofit2.Response

class BeenThereRemoteDataSource(private val dao: BeenThereDatabaseDao) : BeenThereRepository {

    override suspend fun getBooks(title: String, apiKey: String): Response<Books> {
        return super.getBooks(title, apiKey)
    }


}
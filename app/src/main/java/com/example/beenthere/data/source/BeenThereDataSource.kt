package com.example.beenthere.data.source

import androidx.lifecycle.LiveData
import com.example.beenthere.data.Experience
import com.example.beenthere.model.Books
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class BeenThereDataSource(private val dao: BeenThereDatabaseDao) : BeenThereRepository {


    // Api
    override suspend fun getBooks(title: String, apiKey: String): Response<Books> {

        return super.getBooks(title, apiKey)

    }


    // Room
    override suspend fun insertExp(experience: Experience) {
        withContext(Dispatchers.IO) {
            dao.insert(experience)
        }
    }

    override fun getExp(): LiveData<List<Experience>> {
        return dao.getAllExps()
    }

    override suspend fun clearExpInRoom() {
        withContext(Dispatchers.IO) {
            dao.clearExp()
        }
    }


}
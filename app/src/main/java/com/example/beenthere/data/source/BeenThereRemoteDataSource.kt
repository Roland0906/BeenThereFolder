package com.example.beenthere.data.source

import androidx.lifecycle.LiveData
import com.example.beenthere.data.Experience
import com.example.beenthere.model.Books
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class BeenThereRemoteDataSource(private val dao: BeenThereDatabaseDao) : BeenThereRepository {


//    override suspend fun getBooks(title: String, commonQueryParams: Map<String, String>): Response<Books> {
//
//
//
//        return super.getBooks(title, commonQueryParams)
//
//    }


    override suspend fun getBooks(title: String, apiKey: String): Response<Books> {

//        withContext(Dispatchers.IO) {
//            val playlist = DevByteNetwork.devbytes.getPlaylist()
//            database.videoDao.insertAll(playlist.asDatabaseModel())
//        }

        return super.getBooks(title, apiKey)

    }

    override suspend fun insertExp(experience: Experience) {
        withContext(Dispatchers.IO) {
            dao.insert(experience)
        }
    }


}
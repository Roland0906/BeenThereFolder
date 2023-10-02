package com.example.beenthere.data.source

import com.example.beenthere.data.Experience
import com.example.beenthere.data.Situation
import com.example.beenthere.model.Books
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response

class BeenThereDataSource(private val dao: BeenThereDatabaseDao) : BeenThereRepository {


//    override fun setFirebaseListener(
//        db: FirebaseFirestore,
//        collection: String
//    ): Flow<List<Experience>> {
//        return super.setFirebaseListener(db, collection)
//    }

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

    override suspend fun upsertSituation(situation: Situation) {
        withContext(Dispatchers.IO) {
            dao.upsert(situation)
        }
    }

    override suspend fun updateExp(experience: Experience) {
        withContext(Dispatchers.IO) {
            dao.update(experience)
        }
    }

    override suspend fun insertManyExp(experiences: List<Experience>) {
        withContext(Dispatchers.IO) {
            dao.insert(experiences)
        }
    }

    override fun getExp(): Flow<List<Experience>> {
        return dao.getAllExp()
    }

    override fun getSituations(): Flow<List<Situation>> {
        return dao.getSituations()
    }

//    override fun getExp(): LiveData<List<Experience>> {
//        return dao.getAllExp()
//    }



    override suspend fun clearExpInRoom() {
        withContext(Dispatchers.IO) {
            dao.clearExp()
        }
    }

    override suspend fun clearSituationInRoom() {
        withContext(Dispatchers.IO) {
            dao.clearSituation()
        }
    }


}
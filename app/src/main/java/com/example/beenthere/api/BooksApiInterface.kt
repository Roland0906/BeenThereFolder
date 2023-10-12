/*
 * Copyright (C) 2021 Google Developer Student Clubs BBSBEC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gdsc.bbsbec.gbooks.api

import com.example.beenthere.model.Books
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap


interface BooksApiInterface {

    @GET(" ")
    suspend fun getBooks(
        @Query("q") inTitle: String,
//        @QueryMap commonQueryParams: Map<String, String>,
        @Query("key") apiKey: String
    ): Response<Books>
}

// can consider simplify returned result
// to get result faster
// not not sure if i need to change response data class

// or add lottie

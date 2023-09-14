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

package com.gdsc.bbsbec.gbooks.model

import com.squareup.moshi.Json


data class VolumeInfo(

    var title: String? = null,
    var subtitle: String? = null,
    var authors: ArrayList<String> = arrayListOf(),
    var publisher: String? = "Publisher details not available",
    var publishedDate: String? = null,
    var description: String? = "No description found",
    var pageCount: Int? = null,
    var printType: String? = null,
    var categories: ArrayList<String> = arrayListOf(),
    var averageRating: Double? = null,
    var ratingsCount: Int? = null,
    var maturityRating: String? = null,
    var allowAnonLogging: Boolean? = null,
    var contentVersion: String? = null,
    var imageLinks: ImageLinks? = ImageLinks(),
    var language: String? = null,
    var previewLink: String? = null,
    var infoLink: String? = null,
    var canonicalVolumeLink: String? = null

)
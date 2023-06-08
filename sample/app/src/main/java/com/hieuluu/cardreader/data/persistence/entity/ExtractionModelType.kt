/*
 *
 *  * Copyright 2023 Hieu Luu (neo). All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.hieuluu.cardreader.data.persistence.entity

/**
 * Created by Hieu Luu (neo) on 08/06/2023
 */
enum class ExtractionModelType {
    EMAIL,
    PHONE,
    URL,
    OTHER
}

data class ExtractionModel(
    val id: Int,
    val scanId: Int,
    val type: ExtractionModelType,
    val content: String
)

fun ExtractionModel.toFilteredTextModel(): FilteredTextModel {
    return FilteredTextModel(
        filteredTextModelId = id,
        scanId = scanId,
        type = type.name.lowercase(),
        content = content
    )
}

fun FilteredTextModel.toExtractionModel(): ExtractionModel {
    val parserType = when (type) {
        "email" -> ExtractionModelType.EMAIL
        "phone" -> ExtractionModelType.PHONE
        "link" -> ExtractionModelType.URL
        else -> ExtractionModelType.OTHER
    }
    return ExtractionModel(
        id = filteredTextModelId,
        scanId = scanId,
        type = parserType,
        content = content
    )
}
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

package com.hieuluu.cardreader.domain.entityextraction

import com.google.mlkit.nl.entityextraction.Entity
import com.google.mlkit.nl.entityextraction.EntityExtraction
import com.google.mlkit.nl.entityextraction.EntityExtractorOptions
import com.hieuluu.cardreader.data.persistence.entity.ExtractionModelType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Created by Hieu Luu (neo) on 08/06/2023
 */
class EntityExtractionUseCase(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val entityExtractor = EntityExtraction.getClient(
        EntityExtractorOptions.Builder(EntityExtractorOptions.ENGLISH).build()
    )

    suspend operator fun invoke(input: String) = withContext(dispatcher) {
        kotlin.runCatching {
            val resultModels = mutableListOf<ExtractionResultModel>()
            val downloadModelResult = entityExtractor.downloadModelIfNeeded().await()
            val annotations = entityExtractor.annotate(input).await()

            annotations.forEach {
                resultModels.addAll(it.entities.map { entity ->
                    val type = when(entity.type) {
                        Entity.TYPE_EMAIL -> ExtractionModelType.EMAIL
                        Entity.TYPE_PHONE -> ExtractionModelType.PHONE
                        Entity.TYPE_URL -> ExtractionModelType.URL
                        else -> ExtractionModelType.OTHER
                    }
                    ExtractionResultModel(
                        type = type,
                        content = it.annotatedText
                    )
                })
            }
            resultModels.toList()
        }
    }
}
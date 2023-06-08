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

package com.hieuluu.cardreader.domain.textextract

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Created by Hieu Luu (neo) on 08/06/2023
 */
class ScanTextFromImageUseCase(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val recognizers = listOf(
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS),
        TextRecognition.getClient(DevanagariTextRecognizerOptions.Builder().build()),
        TextRecognition.getClient(JapaneseTextRecognizerOptions.Builder().build()),
        TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build()),
        TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build())
    )

    suspend operator fun invoke(image: InputImage) = withContext(dispatcher) {
        kotlin.runCatching {
            val detectedText = mutableSetOf<String>()
            recognizers.forEach { textRecognizer ->
                val result = textRecognizer.process(image).await()
                if (result.text.isNotBlank()) {
                    detectedText.add(result.text)
                }
            }

            val completeText = buildString {
                detectedText.forEach { text -> append(text) }
            }
            completeText
        }
    }
}
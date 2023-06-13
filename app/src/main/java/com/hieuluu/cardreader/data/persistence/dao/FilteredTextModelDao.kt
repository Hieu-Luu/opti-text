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

package com.hieuluu.cardreader.data.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hieuluu.cardreader.data.persistence.entity.FilteredTextModel
import kotlinx.coroutines.flow.Flow

/**
 * Created by Hieu Luu (neo) on 08/06/2023
 */
@Dao
interface FilteredTextModelDao {
    @Insert
    suspend fun insertModel(model: FilteredTextModel)

    @Query("SELECT * FROM FILTERED_TEXT_MODEL")
    fun getAllModels(): Flow<List<FilteredTextModel>>

    @Query("SELECT * FROM FILTERED_TEXT_MODEL WHERE scan_id = :scanId")
    fun getModelsByScanId(scanId: Int): Flow<List<FilteredTextModel>>
}
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
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hieuluu.cardreader.data.persistence.entity.Scan
import kotlinx.coroutines.flow.Flow

/**
 * Created by Hieu Luu (neo) on 08/06/2023
 */
@Dao
interface ScanDao {
    @Query("SELECT * FROM scan WHERE is_pinned=0 ORDER BY date_created DESC")
    fun getAllScans(): Flow<List<Scan>>

    @Query("SELECT * FROM scan WHERE is_pinned=1 ORDER BY date_created DESC")
    fun getAllPinnedScans(): Flow<List<Scan>>

    @Insert
    suspend fun insertScan(scan: Scan): Long

    @Delete
    suspend fun deleteScan(scan: Scan)

    @Query("SELECT * FROM scan WHERE scan_id=:id")
    fun getScanById(id: Int): Flow<Scan>

    @Update
    suspend fun updateScan(scan: Scan)
}
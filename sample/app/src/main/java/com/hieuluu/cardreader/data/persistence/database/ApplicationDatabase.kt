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

package com.hieuluu.cardreader.data.persistence.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hieuluu.cardreader.data.persistence.dao.FilteredTextModelDao
import com.hieuluu.cardreader.data.persistence.dao.ScanDao
import com.hieuluu.cardreader.data.persistence.database.converter.DateConverter
import com.hieuluu.cardreader.data.persistence.entity.FilteredTextModel
import com.hieuluu.cardreader.data.persistence.entity.Scan

/**
 * Created by Hieu Luu (neo) on 08/06/2023
 */
@Database(
    entities = [
        Scan::class,
        FilteredTextModel::class
    ],
    version = 1
)
@TypeConverters(
    DateConverter::class
)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract val scanDao: ScanDao
    abstract val filteredTextModelDao: FilteredTextModelDao
}
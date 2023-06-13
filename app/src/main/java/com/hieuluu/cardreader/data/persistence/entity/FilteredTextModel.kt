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

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Created by Hieu Luu (neo) on 08/06/2023
 */
@Entity(
    tableName = "filtered_text_model",
    foreignKeys = [
        ForeignKey(
            entity = Scan::class,
            parentColumns = ["scan_id"],
            childColumns = ["scan_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FilteredTextModel(
    @ColumnInfo(name = "filtered_text_model_id") @PrimaryKey(autoGenerate = true) val filteredTextModelId: Int = 0,
    @ColumnInfo(name = "scan_id") val scanId: Int,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "content") val content: String
)

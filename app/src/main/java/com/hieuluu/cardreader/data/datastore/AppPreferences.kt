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

package com.hieuluu.cardreader.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Created by Hieu Luu (neo) on 08/06/2023
 */
val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "scan_me_prefs")
class AppPreferences(private val dataStore: DataStore<Preferences>) {
    private companion object {
        val FIRST_LAUNCH = booleanPreferencesKey(name = "first_launch")
        val SHOW_REWARD = booleanPreferencesKey(name = "show_reward")
        val SCAN_COUNT = intPreferencesKey(name = "scan_count")
    }

    val showReward: Flow<Boolean>
        get() = dataStore.data.map {
            it[SHOW_REWARD] ?: false
        }

    val scanCount: Flow<Int>
        get() = dataStore.data.map {
            it[SCAN_COUNT] ?: 6
        }

    val isFirstLaunch: Flow<Boolean>
        get() = dataStore.data.map {
            it[FIRST_LAUNCH] ?: false
        }

    suspend fun firstLaunchComplete() {
        dataStore.edit {
            it[FIRST_LAUNCH] = true
        }
    }

    suspend fun showReward() {
        dataStore.edit {
            it[SHOW_REWARD] = true
        }
    }

    suspend fun rewardShown() {
        dataStore.edit {
            it[SHOW_REWARD] = false
        }
    }

    suspend fun incrementSupportCount() {
        dataStore.edit {
            var current = it[SCAN_COUNT] ?: 6
            current += 1
            it[SCAN_COUNT] = current
        }
    }
}
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

package com.hieuluu.cardreader.di

import com.hieuluu.cardreader.data.repository.FilteredTextRepository
import com.hieuluu.cardreader.data.repository.ScanRepository
import org.koin.dsl.module

/**
 * Created by Hieu Luu (neo) on 08/06/2023
 */
val repositoryModule = module {
    single { ScanRepository(database = get()) }
    single { FilteredTextRepository(database = get()) }
}
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

import com.hieuluu.cardreader.ui.detailscan.DetailScanViewModel
import com.hieuluu.cardreader.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.scope.get
import org.koin.dsl.module

/**
 * Created by Hieu Luu (neo) on 08/06/2023
 */
val viewModelModule = module {
    viewModel {
        HomeViewModel(
            scanRepo = get(),
            filteredTextModelRepo = get(),
            prefs = get(),
            scanTextFromImageUseCase = get(),
            entityExtractionUseCase = get()
        )
    }

    viewModel {
        DetailScanViewModel(
            savedStateHandle = get(),
            scanRepository = get(),
            filteredModelsRepository = get()
        )
    }
}
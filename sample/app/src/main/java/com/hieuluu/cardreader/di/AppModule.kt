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

import android.content.Context
import androidx.room.Room
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.hieuluu.cardreader.data.datastore.AppPreferences
import com.hieuluu.cardreader.data.datastore.datastore
import com.hieuluu.cardreader.data.persistence.database.ApplicationDatabase
import com.hieuluu.cardreader.domain.entityextraction.EntityExtractionUseCase
import com.hieuluu.cardreader.domain.textextract.ScanTextFromImageUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Created by Hieu Luu (neo) on 07/06/2023
 */
val appModule  = module {
    single { provideDatabase(context = androidContext()) }
//    single { providePdfExportService() }
    factory { providePreferences(androidContext()) }
//    single { provideFilterTextService() } bind TextFilterService::class
    factory { provideEntityExtractionUseCase() }
    factory { provideScanTextFromImageUseCase() }
//    single { provideBillingService(androidContext()) }
//    single { provideReviewManager(androidContext()) }
}

private fun provideDatabase(context: Context) =
    Room.databaseBuilder(
        context,
        ApplicationDatabase::class.java,
        "scan_me_database"
    ).fallbackToDestructiveMigration().build()

//private fun providePdfExportService() =
//    PdfExportServiceImpl()

private fun providePreferences(context: Context) = AppPreferences(context.datastore)

//private fun provideFilterTextService() =
//    TextFilterServiceImpl()

private fun provideScanTextFromImageUseCase() =
    ScanTextFromImageUseCase()

//private fun provideBillingService(context: Context) =
//    BillingClientService(context)

//private fun provideReviewManager(context: Context): ReviewManager =
//    ReviewManagerFactory.create(context)

private fun provideEntityExtractionUseCase() =
    EntityExtractionUseCase()
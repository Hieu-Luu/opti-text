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

package com.hieuluu.cardreader

import android.app.Application
import com.hieuluu.cardreader.di.appModule
import com.hieuluu.cardreader.di.repositoryModule
import com.hieuluu.cardreader.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Created by Hieu Luu (neo) on 07/06/2023
 */
class ScanMeApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ScanMeApp)
            modules(listOf(appModule, repositoryModule, viewModelModule))
        }
    }
}
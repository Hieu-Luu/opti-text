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

package com.hieuluu.cardreader.ui.home.reward

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.hieuluu.cardreader.databinding.FragmentScanHomeBinding
import com.hieuluu.cardreader.ui.theme.ScanMeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

/**
 * Created by Hieu Luu (neo) on 08/06/2023
 */
fun FragmentScanHomeBinding.setupComposeSnackBar(
    rewardCount: Flow<Boolean>,
    onRewardShown: () -> Unit
) {
    composeViewSnackBar.apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            ScanMeTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                
                LaunchedEffect(Unit) {
                    rewardCount.collect {
                        delay(1000)
                        if (it) {
                            snackbarHostState.showSnackbar("Rewarded")
                            onRewardShown()
                        }
                    }
                }
                RewardToast(snackbarHostState = snackbarHostState)
            }
        }
    }
}
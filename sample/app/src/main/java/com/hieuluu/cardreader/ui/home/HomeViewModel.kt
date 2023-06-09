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

package com.hieuluu.cardreader.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.hieuluu.cardreader.BuildConfig
import com.hieuluu.cardreader.data.datastore.AppPreferences
import com.hieuluu.cardreader.data.persistence.entity.FilteredTextModel
import com.hieuluu.cardreader.data.persistence.entity.Scan
import com.hieuluu.cardreader.data.repository.FilteredTextRepository
import com.hieuluu.cardreader.data.repository.ScanRepository
import com.hieuluu.cardreader.domain.entityextraction.EntityExtractionUseCase
import com.hieuluu.cardreader.domain.entityextraction.ExtractionResultModel
import com.hieuluu.cardreader.domain.textextract.ScanTextFromImageUseCase
import com.hieuluu.cardreader.util.getCurrentDateTime
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Created by Hieu Luu (neo) on 08/06/2023
 */
class HomeViewModel(
    private val prefs: AppPreferences,
    private val scanRepo: ScanRepository,
    private val filteredTextModelRepo: FilteredTextRepository,
    private val scanTextFromImageUseCase: ScanTextFromImageUseCase,
    private val entityExtractionUseCase: EntityExtractionUseCase
) : ViewModel() {
    private val _events = Channel<HomeEvents>(capacity = 1)
    val events = _events.receiveAsFlow()

    private val isLoading = MutableStateFlow(true)
    private var homeFragActive = true

    private val listOfScans = scanRepo.allScans
        .distinctUntilChanged()
        .onEach { isLoading.value = false }

    private val listOfPinnedScans = scanRepo.allPinnedScans
        .distinctUntilChanged()
        .onEach { isLoading.value = false }

    private val supportCount = prefs.scanCount
        .onEach {
            val hasSeen = prefs.isFirstLaunch.first()
            if (it % 12 == 0 && hasSeen) {
                _events.send(HomeEvents.ShowSupportDialog)
                prefs.incrementSupportCount()
            }
        }
        .launchIn(viewModelScope)

    val showReward = prefs.showReward
    val state = combine(
        isLoading,
        listOfScans,
        listOfPinnedScans
    ) { loading, scans, pinnedScans ->
        HomeUiState(
            isLoading = loading,
            scans = scans,
            pinnedScans = pinnedScans
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), HomeUiState())

    init {
        initOnBoarding()
    }

    fun handlePermissionDenied() = viewModelScope.launch {
        _events.send(HomeEvents.ShowPermissionInfo)
    }

    private fun createScan(text: String, extractionResultModels: List<ExtractionResultModel>) = viewModelScope.launch {
        if (text.isNotEmpty() or text.isNotBlank()) {
            val scan = Scan(
                scanText = text,
                dateCreated = getCurrentDateTime(),
                dateModified = getCurrentDateTime(),
                scanTitle = "",
                isPinned = false
            )

            val result = scanRepo.insertScan(scan)
            val scanId = result.toInt()

            extractionResultModels.forEach { extractedModel ->
                val model = FilteredTextModel(
                    scanId = scanId,
                    type = extractedModel.type.name.lowercase(),
                    content = extractedModel.content
                )
                filteredTextModelRepo.insertModel(model)
                if (BuildConfig.DEBUG) Log.d("hieult", "createScan: model inserted ${model.content}")
            }

            if(homeFragActive) {
                _events.send(HomeEvents.ShowCurrentScanSaved(scanId)).also {
                    prefs.incrementSupportCount()
                }
            }
        } else {
            _events.send(HomeEvents.ShowScanEmpty)
        }
    }

    private fun showLoadingDialog() = viewModelScope.launch {
        _events.send(HomeEvents.ShowLoadingDialog)
    }

    fun deleteScan(scan: Scan) = viewModelScope.launch {
        scanRepo.deleteScan(scan)
        _events.send(HomeEvents.ShowUndoDeleteScan(scan))
    }

    fun insertScan(scan: Scan) = viewModelScope.launch {
        scanRepo.insertScan(scan)
    }

    private fun initOnBoarding() = viewModelScope.launch {
        val hasSeen = prefs.isFirstLaunch.first()
        if (!hasSeen) {
            _events.send(HomeEvents.ShowOnBoarding).also {
                prefs.incrementSupportCount()
            }
        }
    }

    fun handleScan(image: InputImage) {
        showLoadingDialog()
        viewModelScope.launch {
            val completeTextResult = scanTextFromImageUseCase(image)
            completeTextResult.onSuccess { completeText ->
                val extractedEntitiesResult = entityExtractionUseCase(completeText)
                    .fold(
                        onSuccess = { it },
                        onFailure = { emptyList() }
                    )
                createScan(completeText, extractedEntitiesResult)
            }
            completeTextResult.onFailure {
                Log.e("hieult", "Error: ${it.localizedMessage}")
                _events.send(HomeEvents.ShowErrorWhenScanning)
            }
        }
    }

    fun showReward() {
        viewModelScope.launch {
            prefs.showReward()
        }
    }

    fun rewardShown() {
        viewModelScope.launch {
            prefs.rewardShown()
        }
    }

    fun onHomeFrag() { homeFragActive = true }

    fun moveAwayFromScreen() {
        homeFragActive = false
    }
}
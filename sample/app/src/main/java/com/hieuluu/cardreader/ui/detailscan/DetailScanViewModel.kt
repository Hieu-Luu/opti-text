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

package com.hieuluu.cardreader.ui.detailscan

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hieuluu.cardreader.data.persistence.entity.toExtractionModel
import com.hieuluu.cardreader.data.repository.FilteredTextRepository
import com.hieuluu.cardreader.data.repository.ScanRepository
import com.hieuluu.cardreader.util.ScanConst
import com.hieuluu.cardreader.util.getCurrentDateTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Created by Hieu Luu (neo) on 09/06/2023
 */
class DetailScanViewModel(
    val savedStateHandle: SavedStateHandle,
    private val scanRepository: ScanRepository,
    private val filteredModelsRepository: FilteredTextRepository
) : ViewModel() {
    private val args = DetailScanFragmentArgs.fromSavedStateHandle(savedStateHandle)
    private val loading = MutableStateFlow(true)
    private val scan = scanRepository.getScanById(args.scanId)
    @OptIn(ExperimentalCoroutinesApi::class)
    private val filteredModels = scan
        .flatMapLatest {
            filteredModelsRepository.getModelsByScanId(it.scanId.toInt())
        }
        .map {
            it.map { filteredTextModel -> filteredTextModel.toExtractionModel() }
        }
        .onEach {
            loading.value = false
        }

    val state = combine(
        loading,
        scan,
        filteredModels
    ) { isLoading, scan, textModels ->
        DetailScanUiState(
            isLoading = isLoading,
            scan = scan,
            filteredTextModels = textModels,
            isCreated = args.isCreated
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), DetailScanUiState())

    private val scanTitle = savedStateHandle.getStateFlow(ScanConst.SCAN_TITLE, state.value.scan?.scanTitle)
    private val scanContent = savedStateHandle.getStateFlow(ScanConst.SCAN_CONTENT, state.value.scan?.scanText)

    @OptIn(FlowPreview::class)
    private val updateTitleJob = scanTitle
        .debounce(200)
        .filterNotNull()
        .onEach { title ->
            val updatedScan = state.value.scan?.copy(
                scanTitle = title,
                dateModified = getCurrentDateTime()
            )
            updatedScan?.let { scanRepository.updateScan(it) }
        }
        .launchIn(viewModelScope)

    @OptIn(FlowPreview::class)
    private val updateContentJob = scanContent
        .debounce(200)
        .filterNotNull()
        .onEach { content ->
            val updatedScan = state.value.scan?.copy(
                scanText = content,
                dateModified = getCurrentDateTime()
            )
            updatedScan?.let { scanRepository.updateScan(it) }
        }

    fun onTitleChanged(newValue: String) = savedStateHandle.set(ScanConst.SCAN_TITLE, newValue)
    fun onContentChanged(newValue: String) = savedStateHandle.set(ScanConst.SCAN_CONTENT, newValue)

    fun deleteScan() = viewModelScope.launch {
        val current = state.value.scan
        current?.let { scanRepository.deleteScan(it) }
    }

    fun updateScanPinned() = viewModelScope.launch {
        state.value.scan?.let {
            val updatedScan = it.copy(isPinned = !it.isPinned)
            scanRepository.updateScan(updatedScan)
        }
    }
}
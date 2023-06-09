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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hieuluu.cardreader.R
import com.hieuluu.cardreader.data.persistence.entity.ExtractionModel
import com.hieuluu.cardreader.data.persistence.entity.Scan
import com.hieuluu.cardreader.ui.detailscan.components.ScanDetailBottomBar
import com.hieuluu.cardreader.ui.detailscan.components.ScanDetailHeader
import com.hieuluu.cardreader.ui.detailscan.components.ScanDetailTopBar
import com.hieuluu.cardreader.ui.detailscan.components.ScanDetailTopBarState
import com.hieuluu.cardreader.ui.detailscan.components.ScanMeLoadingScreen
import com.hieuluu.cardreader.ui.detailscan.components.ScanMeTextField
import com.hieuluu.cardreader.ui.detailscan.components.TextEntityChip
import com.hieuluu.cardreader.util.dateAsString

/**
 * Created by Hieu Luu (neo) on 09/06/2023
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScanMeDetailScreen(
    modifier: Modifier = Modifier,
    state: DetailScanUiState,
    onTitleTextChanged: (String) -> Unit,
    onContentChanged: (String) -> Unit,
    onPinClicked: () -> Unit,
    onChipClicked: (ExtractionModel) -> Unit,
    onBackClick: () -> Unit,
    onPdfExport:  () -> Unit,
    onDeleteClick: () -> Unit,
    onCopyClick: () -> Unit,
    onShareClick: () -> Unit,
    onTtsClick: () -> Unit,
    onTranslateClick: () -> Unit
) {
    val topBarHeight = 72.dp
    val columnScrollState = rememberLazyListState()

    val topBarState by remember {
        derivedStateOf {
            if (columnScrollState.firstVisibleItemIndex > 0)
                ScanDetailTopBarState.NORMAL
            else ScanDetailTopBarState.EXPANDED
        }
    }

    if (state.isLoading) {
        ScanMeLoadingScreen()
    }

    Box(
        modifier = Modifier
            .then(modifier)
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            state = columnScrollState
        ) {
            item(key = "top_spacer") {
                Spacer(modifier = Modifier.height(56.dp))
            }

            item(key = "scan_header") {
                state.scan?.let { scan ->
                    ScanDetailHeader(
                        title = scan.scanTitle,
                        onTitleChanged = { onTitleTextChanged(it) },
                        dateCreated = stringResource(
                            R.string.text_date_created,
                            dateAsString(scan.dateCreated)
                        ),
                        dateModified = stringResource(
                            R.string.text_date_modified,
                            dateAsString(scan.dateModified)
                        ),
                        isCreated = state.isCreated
                    )
                }
            }

            if (state.filteredTextModels.isNotEmpty()) {
                item {
                    LazyHorizontalStaggeredGrid(
                        rows = StaggeredGridCells.Fixed(2),
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .height(88.dp)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(
                            2.dp,
                            alignment = Alignment.CenterVertically
                        ),
                        horizontalItemSpacing = 4.dp
                    ) {
                        items(
                            items = state.filteredTextModels,
                            key = {it.id}
                        ) {
                            TextEntityChip(
                                entity = it,
                                onClick = { onChipClicked(it) }
                            )
                        }
                    }
                }
            }

            state.scan?.let { scan ->
                item(key = "content_field") {
                    var content by remember { mutableStateOf(scan.scanText) }
                    ScanMeTextField(
                        modifier = Modifier.fillMaxWidth(),
                        text = content,
                        onTextChanged =  {
                            content = it
                            onContentChanged(it)
                        },
                        fontSize = 17.sp
                    )
                }
            }

            item(key = "bottom_spacer") {
                Spacer(modifier = Modifier.height(82.dp))
            }
        }

        ScanDetailTopBar(
            height = topBarHeight,
            topBarState = topBarState,
            modifier = Modifier
                .align(Alignment.TopEnd),
            isPinned = state.scan?.isPinned ?: false,
            onPinClicked = onPinClicked,
            onBackClicked = onBackClick,
            onPdfExportClicked = onPdfExport,
            onDeleteClicked = onDeleteClick,
            onSaveClicked = { } //can do nothing
        )

        ScanDetailBottomBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding(),
            onCopyClick = onCopyClick,
            onShareClick = onShareClick,
            onTosClick = onTtsClick,
            onTranslateClick = onTranslateClick
        )
    }
}
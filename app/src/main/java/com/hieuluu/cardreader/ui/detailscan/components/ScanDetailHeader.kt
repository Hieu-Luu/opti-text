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

package com.hieuluu.cardreader.ui.detailscan.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester

/**
 * Created by Hieu Luu (neo) on 09/06/2023
 */
@Composable
fun ScanDetailHeader(
    modifier: Modifier = Modifier,
    title: String,
    onTitleChanged: (String) -> Unit,
    dateCreated: String,
    dateModified: String,
    isCreated: Int
) {
    var titleText by remember { mutableStateOf(title) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = isCreated) {
        if (isCreated == 1) focusRequester.requestFocus()
    }

    Column(modifier = Modifier.then(modifier)) {
        ScanMeTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            text = titleText,
            onTextChanged = {
                titleText = it
                onTitleChanged(it)
            },
            maxLines = 2
        )
        ScanMeDateText(text = dateCreated)
        ScanMeDateText(text = dateModified)
    }
}

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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.Copy
import compose.icons.tablericons.Ear
import compose.icons.tablericons.Language
import compose.icons.tablericons.Share

@Composable
fun ScanDetailBottomBar(
    modifier: Modifier = Modifier,
    iconButtonSize: Dp = 52.dp,
    iconSize: Dp = 28.dp,
    shape: Shape = RoundedCornerShape(18.dp),
    onCopyClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onTosClick: () -> Unit = {},
    onTranslateClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .then(modifier),
        tonalElevation = 4.dp,
        shape = shape,
        color = MaterialTheme.colorScheme.primary
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = onCopyClick, modifier = Modifier.size(iconButtonSize)) {
                Icon(
                    imageVector = TablerIcons.Copy,
                    contentDescription = "",
                    modifier = Modifier.size(iconSize)
                )
            }
            IconButton(onClick = onShareClick, modifier = Modifier.size(iconButtonSize)) {
                Icon(
                    imageVector = TablerIcons.Share,
                    contentDescription = "",
                    modifier = Modifier.size(iconSize)
                )
            }
            IconButton(onClick = onTosClick, modifier = Modifier.size(iconButtonSize)) {
                Icon(
                    imageVector = TablerIcons.Ear,
                    contentDescription = "",
                    modifier = Modifier.size(iconSize)
                )
            }
            IconButton(onClick = onTranslateClick, modifier = Modifier.size(iconButtonSize)) {
                Icon(
                    imageVector = TablerIcons.Language,
                    contentDescription = "",
                    modifier = Modifier.size(iconSize)
                )
            }
        }
    }
}
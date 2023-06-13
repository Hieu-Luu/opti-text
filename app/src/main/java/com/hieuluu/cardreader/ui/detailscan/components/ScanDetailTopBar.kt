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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowLeft
import compose.icons.tablericons.CircleCheck
import compose.icons.tablericons.FileExport
import compose.icons.tablericons.Pin
import compose.icons.tablericons.Track
import compose.icons.tablericons.Trash

/**
 * Created by Hieu Luu (neo) on 09/06/2023
 */
enum class ScanDetailTopBarState {
    NORMAL, EXPANDED
}

@Composable
fun ScanDetailTopBar(
    modifier: Modifier = Modifier,
    iconButtonSize: Dp = 48.dp,
    iconSize: Dp = 28.dp,
    shape: Shape = RoundedCornerShape(18.dp),
    height: Dp = 72.dp,
    topBarState: ScanDetailTopBarState = ScanDetailTopBarState.EXPANDED,
    isPinned: Boolean = false,
    onBackClicked: () -> Unit = {},
    onPdfExportClicked: () -> Unit = {},
    onDeleteClicked: () -> Unit = {},
    onPinClicked: () -> Unit = {},
    onSaveClicked: () -> Unit = {},
) {
    val transitionData = updateTopBarTransitionData(state = topBarState)
    
    val pinColor by animateColorAsState(
        targetValue = if (isPinned) MaterialTheme.colorScheme.onPrimary else Color.Transparent
    )
    
    Surface(
        modifier = Modifier
            .fillMaxWidth(transitionData.width)
            .requiredHeight(height)
            .padding(8.dp)
            .then(modifier),
        tonalElevation = transitionData.elevation,
        shape = shape,
        border = BorderStroke(width = 1.dp, color = transitionData.outlineColor),
        color = MaterialTheme.colorScheme.background
    ) {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onPrimary) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AnimatedVisibility(
                    visible = topBarState == ScanDetailTopBarState.EXPANDED
                ) {
                    IconButton(
                        onClick = { onBackClicked() },
                        modifier = Modifier.size(iconButtonSize)
                    ) {
                        Icon(
                            imageVector = TablerIcons.ArrowLeft,
                            contentDescription = "",
                            modifier = Modifier.size(iconSize)
                        )

                    }
                }

                Row {
                    // Export to pdf button
                    IconButton(
                        onClick = { onPdfExportClicked() },
                        modifier = Modifier.size(iconButtonSize)
                    ) {
                        Icon(
                            imageVector = TablerIcons.FileExport,
                            contentDescription = "",
                            modifier = Modifier.size(iconSize)
                        )
                    }

                    // Pin button
                    IconButton(
                        onClick = { onPinClicked() },
                        modifier = Modifier
                            .size(iconButtonSize)
                            .drawBehind {
                                drawRoundRect(
                                    color = pinColor,
                                    size = Size(width = size.width, height = size.height / 8f),
                                    cornerRadius = CornerRadius(10.dp.toPx()),
                                    topLeft = Offset(x = 0f, y = size.height)
                                )
                            }
                    ) {
                        Icon(
                            imageVector = TablerIcons.Pin,
                            contentDescription = "",
                            modifier = Modifier.size(iconSize)
                        )
                    }

                    // Delete button
                    IconButton(
                        onClick = { onDeleteClicked() },
                        modifier = Modifier.size(iconButtonSize)
                    ) {
                        Icon(
                            imageVector = TablerIcons.Trash,
                            contentDescription = "",
                            modifier = Modifier.size(iconSize)
                        )
                    }

                    // Save button
                    IconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.size(iconButtonSize)
                    ) {
                        Icon(
                            imageVector = TablerIcons.CircleCheck,
                            contentDescription = "",
                            modifier = Modifier.size(iconSize)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun updateTopBarTransitionData(state: ScanDetailTopBarState): TopBarTransitionData {
    val transition = updateTransition(targetState = state, label = "")

    val outlineColor = transition.animateColor(
        label = ""
    ) {
        when(it) {
            ScanDetailTopBarState.NORMAL -> MaterialTheme.colorScheme.secondaryContainer
            ScanDetailTopBarState.EXPANDED -> Color.Transparent
        }
    }
    
    val elevation = transition.animateDp(
        label = "",
        targetValueByState = {
            when(it) {
                ScanDetailTopBarState.NORMAL -> 4.dp
                ScanDetailTopBarState.EXPANDED -> 0.dp
            }
        }
    )
    
    val width = transition.animateFloat(
        label = "",
        transitionSpec = { tween() }
    ) {
        when(it) {
            ScanDetailTopBarState.NORMAL -> 0.525f
            ScanDetailTopBarState.EXPANDED -> 1f
        }
    }
    
    return remember(transition) {
        TopBarTransitionData(elevation, width, outlineColor)
    }
}

private class TopBarTransitionData(
    elevation: State<Dp>,
    width: State<Float>,
    outlineColor: State<Color>
) {
    val elevation by elevation
    val width by width
    val outlineColor by outlineColor
}
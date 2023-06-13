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

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hieuluu.cardreader.R
import com.hieuluu.cardreader.ui.theme.HeavyBlue
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.Gratipay

/**
 * Created by Hieu Luu (neo) on 08/06/2023
 */
@Composable
fun RewardToast(
    snackbarHostState: SnackbarHostState
) {
    Box(modifier = Modifier.fillMaxSize()) {
        SnackbarHost(
            modifier = Modifier.align(Alignment.TopCenter),
            hostState = snackbarHostState,
            snackbar = {
                RewardSnackbar()
            }
        )
    }
}

@Composable
fun RewardSnackbar() {
    Card(
        modifier = Modifier.padding(14.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(HeavyBlue),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(
                    6.dp,
                    alignment = Alignment.CenterVertically
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(id = R.string.purchase_verified),
                    fontSize = 12.sp,
                    color = Color.White,
                    modifier = Modifier.alpha(0.7f)
                )
                Text(
                    text = stringResource(id = R.string.open_source),
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
            RewardIconAnimated()
        }
    }
}

@Composable
fun RewardIconAnimated() {
    val transition = rememberInfiniteTransition()
    val color by transition.animateColor(
        initialValue = Color(0xFF31ED31),
        targetValue = Color(0xFF2CEEF0),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Icon(
        imageVector = LineAwesomeIcons.Gratipay,
        contentDescription = "",
        tint = color,
        modifier = Modifier.size(64.dp)
    )
}
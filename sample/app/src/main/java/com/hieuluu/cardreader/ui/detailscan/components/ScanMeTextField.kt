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

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hieuluu.cardreader.R
import com.hieuluu.cardreader.ui.theme.TextColorGray

/**
 * Created by Hieu Luu (neo) on 09/06/2023
 */
@Composable
fun ScanMeDateText(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 2.dp)
            .then(modifier),
        text = text,
        fontSize = 14.sp,
        color = TextColorGray
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanMeTextField(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 22.sp,
    maxLines: Int = Int.MAX_VALUE,
    text: String,
    onTextChanged: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.then(modifier),
        value = text,
        onValueChange =  onTextChanged,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            cursorColor = MaterialTheme.colorScheme.primaryContainer,
            textColor = MaterialTheme.colorScheme.primaryContainer,
            disabledBorderColor = Color.Transparent,
            errorBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        ),
        placeholder = {
            Text(
              text = stringResource(id = R.string.enter_title),
              color = Color.LightGray,
              fontSize = fontSize
            )
        },
        textStyle = TextStyle(fontSize = fontSize),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        maxLines = maxLines
    )
}
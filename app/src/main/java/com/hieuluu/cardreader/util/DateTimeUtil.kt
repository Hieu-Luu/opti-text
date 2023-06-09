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

package com.hieuluu.cardreader.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.logging.SimpleFormatter

/**
 * Created by Hieu Luu (neo) on 08/06/2023
 */
fun getCurrentDateTime(): Long {
    return Calendar.getInstance().time.time
}

fun dateAsString(
    dateInMillis: Long,
    format: String = "dd.MM.yyyy HH:mm",
    locale: Locale = Locale.getDefault()
): String {
    val date = Date(dateInMillis)
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(date)
}
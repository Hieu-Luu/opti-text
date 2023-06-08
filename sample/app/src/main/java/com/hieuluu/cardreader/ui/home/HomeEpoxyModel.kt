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

import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.hieuluu.cardreader.R
import com.hieuluu.cardreader.data.persistence.entity.Scan
import com.hieuluu.cardreader.databinding.ModelPinnedHeaderBinding
import com.hieuluu.cardreader.databinding.ModelScanHeaderBinding
import com.hieuluu.cardreader.databinding.ModelScanLoadingBarBinding
import com.hieuluu.cardreader.databinding.ModelScanTopBarBinding
import com.hieuluu.cardreader.databinding.ScanListItemBinding
import com.hieuluu.cardreader.framework.epoxy.ViewBindingKotlinModel
import com.hieuluu.cardreader.util.dateAsString

/**
 * Created by Hieu Luu (neo) on 08/06/2023
 */
@EpoxyModelClass
abstract class ScanTopBarEpoxyModel :
    ViewBindingKotlinModel<ModelScanTopBarBinding>(R.layout.model_scan_top_bar) {

    @EpoxyAttribute
    lateinit var onInfoClicked: () -> Unit

    override fun ModelScanTopBarBinding.bind() {
        imageViewInfo.setOnClickListener { onInfoClicked() }
    }
}

@EpoxyModelClass
abstract class ScanHeaderEpoxyModel :
    ViewBindingKotlinModel<ModelScanHeaderBinding>(R.layout.model_scan_header) {
    @EpoxyAttribute
    var numOfScans: String = ""

    override fun ModelScanHeaderBinding.bind() {
        textViewNumOfScans.text = numOfScans
    }
}

@EpoxyModelClass
abstract class ScanListItemEpoxyModel :
    ViewBindingKotlinModel<ScanListItemBinding>(R.layout.scan_list_item) {

        @EpoxyAttribute
        lateinit var scan: Scan

        @EpoxyAttribute
        lateinit var onScanClicked: (Scan) -> Unit

    override fun ScanListItemBinding.bind() {
        val title = scan.scanTitle.ifEmpty { scan.scanText.lines()[0] }

        textViewDate.text = dateAsString(scan.dateModified)
        textViewTitle.text = title
        textViewContent.text = scan.scanText
        card.setOnClickListener { onScanClicked(scan) }
        imageViewPinned.isVisible = scan.isPinned
    }
}

@EpoxyModelClass
abstract class ListHeaderEpoxyModel :
    ViewBindingKotlinModel<ModelPinnedHeaderBinding>(R.layout.model_pinned_header) {
    @EpoxyAttribute
    lateinit var headerTitle: String

    override fun ModelPinnedHeaderBinding.bind() {
        textViewListHeader.text = headerTitle
    }
}

@EpoxyModelClass
abstract class LoadingScansEpoxyModel :
    ViewBindingKotlinModel<ModelScanLoadingBarBinding>(R.layout.model_scan_loading_bar) {
    override fun ModelScanLoadingBarBinding.bind() {}
}




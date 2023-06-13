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

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import com.hieuluu.cardreader.R
import com.hieuluu.cardreader.data.persistence.entity.ExtractionModel
import com.hieuluu.cardreader.data.persistence.entity.ExtractionModelType
import com.hieuluu.cardreader.ui.theme.HeavyBlue
import com.hieuluu.cardreader.ui.theme.LightBlue
import com.hieuluu.cardreader.ui.theme.ScanMeTheme
import com.hieuluu.cardreader.util.ScanConst
import com.hieuluu.cardreader.util.showConfirmDialog
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

/**
 * Created by Hieu Luu (neo) on 09/06/2023
 */
class DetailScanFragment : Fragment() {

    private val viewModel: DetailScanViewModel by viewModel()
    private lateinit var textToSpeech: TextToSpeech
    private val snackbarHostState = SnackbarHostState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            transitionName = ScanConst.TRANSACTION_NAME
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                ScanMeTheme {
                    val state by viewModel.state.collectAsStateWithLifecycle()

                    Box(modifier = Modifier.fillMaxSize()) {
                        ScanMeDetailScreen(
                            state = state,
                            onTitleTextChanged = { viewModel.onTitleChanged(it) },
                            onContentChanged = { viewModel.onContentChanged(it) },
                            onPinClicked = { viewModel.updateScanPinned() },
                            onChipClicked = { launchExtractedModelIntent(it) },
                            onBackClick = { findNavController().navigateUp() },
                            onPdfExport = { navigateToPdfExport(state.scan?.scanId) },
                            onDeleteClick = { onDeleteClick() },
                            onCopyClick = { copyToClipboard(state.scan?.scanText) },
                            onTranslateClick = { openInTranslate(state.scan?.scanText) },
                            onShareClick = { share(state.scan?.scanText) },
                            onTtsClick = { tts(state.scan?.scanText) }
                        )

                        SnackbarHost(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 72.dp)
                                .navigationBarsPadding(),
                            hostState = snackbarHostState,
                            snackbar = {
                                Snackbar(
                                    snackbarData = it,
                                    containerColor = LightBlue,
                                    contentColor = HeavyBlue
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        if (this::textToSpeech.isInitialized) textToSpeech.stop()
        super.onDestroy()
    }

    private fun openInTranslate(input: String?) = input?.let { text ->
        try {
            val intent = Intent()
            intent.apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                putExtra("key_text_input", text)
                putExtra("key_text_output", "")
                putExtra("key_language_from", "en")
                putExtra("key_language_to", "mal")
                putExtra("key_suggest_translation", "")
                putExtra("key_from_floating_window", false)
                component = ComponentName(
                    "com.google.android.apps.translate",
                    "com.google.android.apps.translate.TranslateActivity"
                )
            }
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Log.e("hieult", e.toString())
            //snackbar - it seems you don't have translate installed.
        }
    }

    private fun tts(input: String?) = input?.let { text ->
        textToSpeech = TextToSpeech(requireContext()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val hasLanguage = textToSpeech.setLanguage(Locale.US)
                if (hasLanguage == TextToSpeech.LANG_MISSING_DATA || hasLanguage == TextToSpeech.LANG_NOT_SUPPORTED ) {
                    lifecycleScope.launch {
                        snackbarHostState.showSnackbar(message = getString(R.string.unsupported_language))
                    }
                } else {
                    lifecycleScope.launch {
                        snackbarHostState.showSnackbar(message = getString(R.string.loading))
                    }
                    textToSpeech.speak(
                        text,
                        TextToSpeech.QUEUE_ADD,
                        null,
                        viewModel.state.value.scan?.scanId.toString()
                    )
                }
            }
        }
    }

    private fun copyToClipboard(input: String?) = input?.let { text ->
        val clipboardManager =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("raw_data", text)
        clipboardManager.setPrimaryClip(clip)
        // fire off snackbar
        lifecycleScope.launch { snackbarHostState.showSnackbar(message = getString(R.string.copied_clip)) }
    }

    private fun share(input: String?) = input?.let { text ->
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val intent = Intent.createChooser(shareIntent, null)
        startActivity(intent)
    }

    private fun navigateToPdfExport(scanId: Long?) = scanId?.let {
//        val action = DetailScanFragmentDirections.toPdfDialogFragment(it.toInt())
//        findNavController().safeNav(action)
    }

    private fun onDeleteClick() {
        showConfirmDialog(
            message = getString(R.string.delete_scanned_text),
            onPositiveClick = {
                viewModel.deleteScan()
                findNavController().navigateUp()
            }
        )
    }

    private fun launchExtractedModelIntent(model: ExtractionModel) {
        try {
            var intent: Intent? = null
            when (model.type) {
                ExtractionModelType.EMAIL -> {
                    intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:")
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(model.content))
                    }
                }
                ExtractionModelType.PHONE -> {
                    intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:${model.content}")
                    }
                }
                ExtractionModelType.URL -> {
                    intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(model.content)
                    }
                }
                ExtractionModelType.OTHER -> copyToClipboard(model.content)
            }
            requireActivity().packageManager?.let {
                intent?.let { startActivity(it) }
            }
        } catch (e: ActivityNotFoundException) {
            lifecycleScope.launch { snackbarHostState.showSnackbar(message = getString(R.string.something_went_wrong)) }
        }
    }
}
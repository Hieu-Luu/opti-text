package com.hieuluu.optitext.ui.extractor

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleObserver
import androidx.viewbinding.ViewBinding
import com.hieuluu.optitext.R
import com.hieuluu.optitext.databinding.FragmentTextExtractorBinding

class TextExtractorFragment : Fragment(), LifecycleObserver {

    private lateinit var viewBinding: ViewBinding

    companion object {
        fun newInstance() = TextExtractorFragment()
    }

    private lateinit var viewModel: TextExtractorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentTextExtractorBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[TextExtractorViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
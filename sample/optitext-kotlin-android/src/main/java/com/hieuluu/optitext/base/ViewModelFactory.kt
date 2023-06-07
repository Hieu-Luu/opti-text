package com.hieuluu.optitext.base

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hieuluu.optitext.ui.detector.StillImageViewModel

class ViewModelFactory(application: Application) :
    ViewModelProvider.Factory {
    var creators = mutableMapOf<Class<out ViewModel>, ViewModel>()

    init {
        creators[StillImageViewModel::class.java] =
            StillImageViewModel()
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        for (a in creators.entries) {
            if (modelClass.isAssignableFrom(a.key)) {
                return a.value as T
            }
        }
        throw IllegalArgumentException("Unknown class name")
    }

}
package com.hieuluu.optitext.base

import android.app.Application
import android.content.res.Configuration
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

/**
 * Created by Hieu Luu (neo) on 06/06/2023
 */
abstract class BaseActivity : AppCompatActivity() {
    lateinit var mBinding: ViewBinding
    abstract var viewController: ViewController
    var configurationChangedListener: ((Configuration) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = getBinding(layoutInflater)
        setContentView(mBinding.root)
        getFragmentContainerId()?.let {
            viewController = ViewController(it, supportFragmentManager, applicationContext)
        }
        setupViewModel()
        initView()
        initData(savedInstanceState)
        initObserver()
    }

    fun getViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(
            this,
            ViewModelFactory(Application())
        )
    }

    open fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onBackPressed() {
        if (onBackPressedDispatcher.hasEnabledCallbacks()) {
            super.onBackPressed()
        } else {
            viewController.popFragment()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val displayMetrics = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(displayMetrics)
        Log.d(
            "BaseActivity",
            "Configuration Changed: \n Window size ${displayMetrics.widthPixels}x${displayMetrics.heightPixels}"
        )
        configurationChangedListener?.invoke(newConfig)
    }

    abstract fun getBinding(inflater: LayoutInflater): ViewBinding
    abstract fun getFragmentContainerId(): Int?
    abstract fun setupViewModel()
    abstract fun initView()
    abstract fun initData(savedInstanceState: Bundle?)
    abstract fun initObserver()

}
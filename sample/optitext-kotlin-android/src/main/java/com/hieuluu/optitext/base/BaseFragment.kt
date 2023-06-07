package com.hieuluu.optitext.base

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.multidex.BuildConfig
import androidx.viewbinding.ViewBinding
import java.lang.Exception

/**
 * Created by Hieu Luu (neo) on 07/06/2023
 */
abstract class BaseFragment : Fragment() {

    lateinit var mBinding: ViewBinding
    private lateinit var mActivity: BaseActivity
    lateinit var childViewController: ViewController

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) this.mActivity = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = getBinding(inflater, container)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewController()
        initView()
        initData()
        initObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideLoading()
    }

    open fun hideKeyboard() {
//        if (mActivity != null) {
//            mActivity!!.hideKeyboard()
//        }
    }

    open fun showKeyboard() {
        val inputMethodManager: InputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    open fun showLoading() {
//        hideLoading()
//        mProgressDialog = CommonUtils.showLoadingDialog(this.context)
    }

    open fun hideLoading() {
//        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
//            mProgressDialog!!.cancel()
//        }
    }

    open fun showMess(str: String?) {
        if (BuildConfig.DEBUG) {
            Toast.makeText(context,
                "${if (str != null) "DEV_CHECK: $str" else "DEV_CHECK: null"}\n" +
                        "Will be disable when release product",
                Toast.LENGTH_LONG)
                .show()
        } else {
            val toast = Toast.makeText(context, str ?: "", Toast.LENGTH_LONG)
            val v = toast.view?.findViewById<TextView>(android.R.id.message)
            v?.gravity = Gravity.CENTER
            toast.show()
        }
    }

    open fun getAccessToken(): String {
        return try {
            ""
//            formatToken(AppPreferencesHelper(context).authToken.accessToken)
        } catch (e: Exception) {
            ""
        }
    }

    open fun formatToken(token: String?): String {
        if (TextUtils.isEmpty(token)) {
//            showMess("Token Empty!")
            return ""
        }
        return "Bearer $token"
    }

    val callbackPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.e("DEBUG", "${it.key} = ${it.value}")
            }
        }

    abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ViewBinding
    abstract fun setViewController()
    abstract fun setupViewModel()
    abstract fun initView()
    abstract fun initData()
    abstract fun initObserver()
}
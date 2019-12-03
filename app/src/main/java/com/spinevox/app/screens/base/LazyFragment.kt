package com.spinevox.app.screens.base

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class LazyFragment<LayoutClassBinding : ViewDataBinding> : Fragment(), CoroutineScope {

    private lateinit var job: Job
    lateinit var sharedPreferences: SharedPreferences


    override val coroutineContext: CoroutineContext by lazy { Dispatchers.Main + job }

    abstract var binding: LayoutClassBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        job = Job()
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.lifecycleOwner = this
        sharedPreferences = activity!!.getSharedPreferences("", Context.MODE_PRIVATE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initController(view)
    }

    open fun initController(view: View) {}

    abstract fun getLayoutId(): Int

}
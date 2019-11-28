package com.leo.spinevox.screens.profile

import android.view.View
import com.leo.spinevox.R
import com.leo.spinevox.databinding.LayoutProfileSettingsBinding
import com.leo.spinevox.screens.base.LazyFragment

class ProfileSettingsFragment : LazyFragment<LayoutProfileSettingsBinding>() {

    override lateinit var binding: LayoutProfileSettingsBinding

    override fun initController(view: View) {

    }

    override fun getLayoutId(): Int = R.layout.layout_profile_settings

}
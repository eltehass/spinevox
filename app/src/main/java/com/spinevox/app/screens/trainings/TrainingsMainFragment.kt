package com.spinevox.app.screens.trainings

import com.spinevox.app.R
import com.spinevox.app.databinding.LayoutMainTrainingsBinding
import com.spinevox.app.screens.base.LazyFragment

class TrainingsMainFragment : LazyFragment<LayoutMainTrainingsBinding>() {

    override lateinit var binding: LayoutMainTrainingsBinding

    override fun getLayoutId(): Int = R.layout.layout_main_trainings
    
}
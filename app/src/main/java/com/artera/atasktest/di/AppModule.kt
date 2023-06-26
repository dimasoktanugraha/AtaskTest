package com.artera.atasktest.di

import com.artera.atasktest.presentation.camera.CameraViewModel
import com.artera.atasktest.presentation.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { MainViewModel() }
    viewModel { CameraViewModel() }
}
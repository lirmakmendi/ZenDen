package com.sceproject.zenden.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sceproject.zenden.R
import com.sceproject.zenden.components.AppScaffold
import com.sceproject.zenden.components.MeasureNowContent
import com.sceproject.zenden.data.viewmodels.HomeViewModel
import com.sceproject.zenden.data.viewmodels.MeasureNowViewModel

@Composable
fun MeasureNowScreen(homeViewModel: HomeViewModel = viewModel(), measureNowViewModel: MeasureNowViewModel = viewModel()) {
    AppScaffold(
        title = stringResource(id = R.string.measure_now),
        homeViewModel = homeViewModel,
        secondaryViewModel = measureNowViewModel,
        content = { paddingValues ->
            MeasureNowContent(paddingValues, measureNowViewModel)
        }
    )
}

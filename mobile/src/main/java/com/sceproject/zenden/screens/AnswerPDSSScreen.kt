package com.sceproject.zenden.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sceproject.zenden.R
import com.sceproject.zenden.components.AnswerPDSSContent
import com.sceproject.zenden.components.AppScaffold
import com.sceproject.zenden.data.viewmodels.HomeViewModel
import com.sceproject.zenden.data.viewmodels.AnswerPDSSViewModel

@Composable
fun AnswerPDSSScreen(homeViewModel: HomeViewModel = viewModel(), AnswerPDSSViewModel: AnswerPDSSViewModel = viewModel()) {
    AppScaffold(
        title = stringResource(id = R.string.answer_pdss),
        homeViewModel = homeViewModel,
        secondaryViewModel = AnswerPDSSViewModel,
        content = { paddingValues ->
            AnswerPDSSContent(paddingValues, AnswerPDSSViewModel)
        }
    )
}

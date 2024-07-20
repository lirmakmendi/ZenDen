package com.sceproject.zenden.screens

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sceproject.zenden.R
import com.sceproject.zenden.components.AppScaffold
import com.sceproject.zenden.components.CheckPanicAttackContent
import com.sceproject.zenden.data.viewmodels.HomeViewModel
import com.sceproject.zenden.data.viewmodels.PredictionViewModel
import com.sceproject.zenden.data.viewmodels.PredictionViewModelFactory
import com.sceproject.zenden.navigation.Screen
import com.sceproject.zenden.navigation.ZenDenAppRouter
import kotlinx.coroutines.delay

@Composable
fun CheckPanicAttackScreen(
    homeViewModel: HomeViewModel = viewModel(),
) {
    val context = LocalContext.current
    val predictionViewModel: PredictionViewModel = viewModel(
        factory = PredictionViewModelFactory(context)
    )

    val age by predictionViewModel.age.observeAsState("")
    val gender by predictionViewModel.gender.observeAsState("")
    val lastPdssMeasurement by predictionViewModel.lastPdssMeasurement.observeAsState(0)
    val lastSdnnMeasurement by predictionViewModel.lastSdnnMeasurement.observeAsState(0f)
    val predictionResult by predictionViewModel.predictionResult.observeAsState(null)
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(age, gender, lastPdssMeasurement, lastSdnnMeasurement) {
        Log.d("CheckPanicAttackScreen", "age: $age, gender: $gender, lastPdssMeasurement: $lastPdssMeasurement, lastSdnnMeasurement: $lastSdnnMeasurement")
        delay(2000) // Simulate loading delay
        predictionViewModel.runPrediction()
        isLoading = false
    }

    AppScaffold(
        title = stringResource(id = R.string.check_panic),
        homeViewModel = homeViewModel,
        secondaryViewModel = predictionViewModel,
        content = { paddingValues ->
            CheckPanicAttackContent(
                paddingValues = paddingValues,
                age = age,
                gender = gender,
                lastPdssMeasurement = lastPdssMeasurement,
                lastSdnnMeasurement = lastSdnnMeasurement,
                isLoading = isLoading,
                predictionResult = predictionResult,
                onHomeClick = { ZenDenAppRouter.navigateTo(Screen.HomeScreen) },
                onRelaxationClick = { ZenDenAppRouter.navigateTo(Screen.RelaxationScreen) }
            )
        }
    )
}

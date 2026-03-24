package com.dev.mealapp.features.meals

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.dev.mealapp.data.model.Meal
import com.dev.mealapp.ui.components.AppBars
import com.dev.mealapp.ui.components.ErrorView
import com.dev.mealapp.ui.components.LoadingView
import com.dev.mealapp.ui.components.MealGrid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealsScreen(
    categoryName: String,
    viewModel: MealsViewModel,
    onBackClick: () -> Unit,
    onMealClick: (Meal) -> Unit
) {
    val viewState by viewModel.mealsState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    LaunchedEffect(categoryName) { viewModel.fetchMealsByCategory(categoryName) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { AppBars.MealAppBar(title = categoryName, onBackClick = onBackClick) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            when {
                viewState.loading -> {
                    LoadingView(Modifier.align(Alignment.Center))
                }

                viewState.error != null -> {
                    ErrorView(
                        error = viewState.error ?: "Unknown error",
                        onRetry = { viewModel.fetchMealsByCategory(categoryName) },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    MealGrid(meals = viewState.list, onMealClick = onMealClick)
                }
            }
        }
    }
}

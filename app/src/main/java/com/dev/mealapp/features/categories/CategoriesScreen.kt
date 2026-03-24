package com.dev.mealapp.features.categories

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dev.mealapp.data.model.Category
import com.dev.mealapp.ui.components.AppBars
import com.dev.mealapp.ui.components.CategoryGrid
import com.dev.mealapp.ui.components.ErrorView
import com.dev.mealapp.ui.components.LoadingView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    viewModel: CategoriesViewModel = viewModel(),
    onCategoryClick: (Category) -> Unit,
    onSearchClick: () -> Unit
) {
    val viewState by viewModel.categoriesState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { AppBars.CategoriesAppBar(onSearchClick = onSearchClick) },
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
                        onRetry = { viewModel.fetchCategories() },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    CategoryGrid(categories = viewState.list, onCategoryClick = onCategoryClick)
                }
            }
        }
    }
}

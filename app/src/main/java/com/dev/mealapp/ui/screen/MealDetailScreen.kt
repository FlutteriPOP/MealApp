package com.dev.mealapp.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.dev.mealapp.data.model.MealDetail
import com.dev.mealapp.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealDetailScreen(
    mealId: String,
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    val viewState by viewModel.mealDetailState.collectAsState()

    LaunchedEffect(mealId) {
        viewModel.fetchMealDetails(mealId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recipe", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                viewState.loading -> {
                    LoadingView(Modifier.align(Alignment.Center))
                }
                viewState.error != null -> {
                    ErrorView(
                        error = viewState.error ?: "Unknown error",
                        onRetry = { viewModel.fetchMealDetails(mealId) },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                viewState.mealDetail != null -> {
                    MealDetailContent(meal = viewState.mealDetail!!)
                }
            }
        }
    }
}

@Composable
fun MealDetailContent(meal: MealDetail) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Box(modifier = Modifier.height(300.dp)) {
            Image(
                painter = rememberAsyncImagePainter(meal.strMealThumb),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Black.copy(alpha = 0.5f), Color.Transparent, Color.Transparent, Color.Black.copy(alpha = 0.7f))
                        )
                    )
            )
            Text(
                text = meal.strMeal,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp)
            )
        }

        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Badge(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                    Text(meal.strCategory, color = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.padding(4.dp))
                }
                Badge(containerColor = MaterialTheme.colorScheme.secondaryContainer) {
                    Text(meal.strArea, color = MaterialTheme.colorScheme.onSecondaryContainer, modifier = Modifier.padding(4.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Ingredients",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            IngredientsList(meal)

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                "Instructions",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = meal.strInstructions,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 28.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun IngredientsList(meal: MealDetail) {
    val ingredients = listOfNotNull(
        meal.strIngredient1 to meal.strMeasure1,
        meal.strIngredient2 to meal.strMeasure2,
        meal.strIngredient3 to meal.strMeasure3,
        meal.strIngredient4 to meal.strMeasure4,
        meal.strIngredient5 to meal.strMeasure5,
        meal.strIngredient6 to meal.strMeasure6,
        meal.strIngredient7 to meal.strMeasure7,
        meal.strIngredient8 to meal.strMeasure8,
        meal.strIngredient9 to meal.strMeasure9,
        meal.strIngredient10 to meal.strMeasure10,
    ).filter { it.first?.isNotEmpty() == true }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ingredients.forEach { (ingredient, measure) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(ingredient!!, fontWeight = FontWeight.SemiBold)
                Text(measure ?: "", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
        }
    }
}

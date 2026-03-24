package com.dev.mealapp.features.mealdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
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
import com.dev.mealapp.ui.components.ErrorView
import com.dev.mealapp.ui.components.LoadingView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealDetailScreen(
    mealId: String,
    viewModel: MealDetailViewModel,
    onBackClick: () -> Unit
) {
    val viewState by viewModel.mealDetailState.collectAsState()

    LaunchedEffect(mealId) {
        viewModel.fetchMealDetails(mealId)
    }

    Scaffold(
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

            // Custom Top Bar overlay
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.3f))
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = { /* TODO */ },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.3f))
                    ) {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite", tint = Color.White)
                    }
                    IconButton(
                        onClick = { /* TODO */ },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.3f))
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White)
                    }
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
        Box(modifier = Modifier.height(400.dp)) {
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
                            listOf(
                                Color.Black.copy(alpha = 0.4f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            meal.strCategory,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Surface(
                        color = Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            meal.strArea,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = meal.strMeal,
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 36.sp
                )
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = (-24).dp),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                Text(
                    "Ingredients",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    "${getIngredientsCount(meal)} items needed",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                IngredientsList(meal)

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    "Preparation",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = meal.strInstructions,
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = 28.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                )
                
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

fun getIngredientsCount(meal: MealDetail): Int {
    return listOfNotNull(
        meal.strIngredient1, meal.strIngredient2, meal.strIngredient3, meal.strIngredient4,
        meal.strIngredient5, meal.strIngredient6, meal.strIngredient7, meal.strIngredient8,
        meal.strIngredient9, meal.strIngredient10
    ).filter { it.isNotEmpty() }.size
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

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ingredients.forEach { (ingredient, measure) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        ingredient!!, 
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(
                    measure ?: "", 
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary, 
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

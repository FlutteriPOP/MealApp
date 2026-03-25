package com.dev.mealapp.features.mealdetail

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.dev.mealapp.data.model.MealDetail
import com.dev.mealapp.ui.components.BadgeExpressive
import com.dev.mealapp.ui.components.ErrorView
import com.dev.mealapp.ui.components.LoadingView
import com.dev.mealapp.ui.components.YoutubePlayer
import com.dev.mealapp.ui.theme.MealAppTheme
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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
                    MealDetailContent(
                        meal = viewState.mealDetail!!,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }

            // Floating Back Button
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(16.dp)
                    .size(40.dp)
                    .background(
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                        CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@SuppressLint("FrequentlyChangingValue")
@Composable
fun MealDetailContent(meal: MealDetail, modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    var isVideoReady by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }

    val imageScale = (1f + scrollState.value * 0.0005f).coerceIn(1f, 1.2f)
    val ingredients = remember(meal) { meal.extractIngredients() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Enhanced Parallax Header Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.1f)
                .graphicsLayer {
                    translationY = scrollState.value * 0.5f
                }
        ) {
            val hasVideo = !meal.strYoutube.isNullOrBlank()
            Crossfade(
                targetState = isVideoReady && hasVideo,
                label = "video_crossfade"
            ) { isReady ->
                if (isReady) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        YoutubePlayer(
                            youtubeUrl = meal.strYoutube!!,
                            modifier = Modifier.fillMaxSize(),
                            onReady = { isVideoReady = true }
                        )
                    }
                } else {
                    Image(
                        painter = rememberAsyncImagePainter(meal.strMealThumb),
                        contentDescription = "Meal Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .scale(imageScale),
                        contentScale = ContentScale.Crop
                    )

                    if (hasVideo) {
                        // Preload video in a hidden box to check readiness
                        Box(
                            modifier = Modifier
                                .size(1.dp)
                                .alpha(0f)
                        ) {
                            YoutubePlayer(
                                youtubeUrl = meal.strYoutube,
                                modifier = Modifier.fillMaxSize(),
                                onReady = { isVideoReady = true }
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Black.copy(alpha = 0.3f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp)
                    .padding(bottom = 30.dp)
            ) {
                BadgeExpressive(
                    text = meal.strCategory,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    textColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = meal.strMeal,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                    )
                )
            }
        }

        // Content Section
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-30).dp),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                val estimatedTime = remember(ingredients) { "${ingredients.size * 5 + 10} min" }
                val estimatedCalories =
                    remember(ingredients) { "${ingredients.size * 45 + 100} kcal" }
                val difficulty = remember(ingredients) {
                    when {
                        ingredients.size > 12 -> "Hard"
                        ingredients.size > 7 -> "Medium"
                        else -> "Easy"
                    }
                }

                // Info Card
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        .padding(vertical = 24.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    QuickInfoItem(
                        Icons.Default.Timer,
                        estimatedTime,
                        "Time",
                        MaterialTheme.colorScheme.primary
                    )
                    QuickInfoItem(
                        Icons.Default.Whatshot,
                        estimatedCalories,
                        "Calories",
                        MaterialTheme.colorScheme.error
                    )
                    QuickInfoItem(
                        Icons.Default.KeyboardArrowUp,
                        difficulty,
                        "Level",
                        MaterialTheme.colorScheme.secondary
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Ingredients",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            "${ingredients.size} items",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    BadgeExpressive(
                        text = meal.strArea,
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        textColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                IngredientsListExpressive(ingredients)

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    "Method",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .animateContentSize()
                            .clickable(
                                onClickLabel = if (isExpanded) "Collapse recipe" else "Expand recipe",
                                role = Role.Button
                            ) { isExpanded = !isExpanded }
                    ) {
                        SelectionContainer {
                            Text(
                                text = meal.strInstructions,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    lineHeight = 28.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = if (isExpanded) Int.MAX_VALUE else 6,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Row(
                            modifier = Modifier.padding(top = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isExpanded) "Show Less" else "Read Full Recipe",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun QuickInfoItem(icon: ImageVector, value: String, label: String, iconColor: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.semantics(mergeDescendants = true) {
            contentDescription = "$label: $value"
        }
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.5).sp
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun IngredientsListExpressive(ingredients: List<Pair<String, String>>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ingredients.forEach { (ingredient, measure) ->
            IngredientItem(ingredient, measure)
        }
    }
}

@Composable
fun IngredientItem(ingredient: String, measure: String) {
    var isChecked by remember { mutableStateOf(false) }
    val animatedAlpha by animateFloatAsState(if (isChecked) 0.6f else 1f, label = "alpha")
    val animatedColor by animateColorAsState(
        if (isChecked) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surfaceContainerLow,
        label = "color"
    )

    Surface(
        onClick = { isChecked = !isChecked },
        shape = RoundedCornerShape(20.dp),
        color = animatedColor,
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                role = Role.Button
                contentDescription =
                    if (isChecked) "Unmark ingredient: $ingredient" else "Mark ingredient: $ingredient"
                onClick(label = if (isChecked) "Unmark" else "Mark") {
                    isChecked = !isChecked
                    true
                }
            }
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .alpha(animatedAlpha),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Ingredient Thumbnail
                Surface(
                    modifier = Modifier.size(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                ) {
                    val encodedIngredient = remember(ingredient) {
                        URLEncoder.encode(ingredient, StandardCharsets.UTF_8.toString())
                    }
                    AsyncImage(
                        model = "https://www.themealdb.com/images/ingredients/${encodedIngredient}-Small.png",
                        contentDescription = "Ingredient: $ingredient",
                        modifier = Modifier.padding(4.dp),
                        contentScale = ContentScale.Fit,
                        error = rememberVectorPainter(Icons.Default.Fastfood),
                        fallback = rememberVectorPainter(Icons.Default.Fastfood)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        ingredient,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None
                        ),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        measure,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = if (isChecked) "Checked" else "Unchecked",
                tint = if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(
                    alpha = 0.2f
                ),
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

fun MealDetail.extractIngredients(): List<Pair<String, String>> {
    return listOfNotNull(
        strIngredient1 to strMeasure1,
        strIngredient2 to strMeasure2,
        strIngredient3 to strMeasure3,
        strIngredient4 to strMeasure4,
        strIngredient5 to strMeasure5,
        strIngredient6 to strMeasure6,
        strIngredient7 to strMeasure7,
        strIngredient8 to strMeasure8,
        strIngredient9 to strMeasure9,
        strIngredient10 to strMeasure10,
        strIngredient11 to strMeasure11,
        strIngredient12 to strMeasure12,
        strIngredient13 to strMeasure13,
        strIngredient14 to strMeasure14,
        strIngredient15 to strMeasure15,
        strIngredient16 to strMeasure16,
        strIngredient17 to strMeasure17,
        strIngredient18 to strMeasure18,
        strIngredient19 to strMeasure19,
        strIngredient20 to strMeasure20,
    ).filter { it.first?.isNotBlank() == true }
        .map { it.first!! to (it.second ?: "") }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun MealDetailPreview() {
    val mockMeal = MealDetail(
        idMeal = "52772",
        strMeal = "Teriyaki Chicken Casserole",
        strMealAlternate = null,
        strMealThumb = "https://www.themealdb.com/images/media/meals/wvpsxx1468256321.jpg",
        strInstructions = "Preheat oven to 350° F. Spray a 9x13-inch baking pan with non-stick spray.\nCombine cooked rice, chicken and vegetables in a large mixing bowl and set aside.\nIn a small saucepan, combine the soy sauce, water, brown sugar, ginger and garlic powder and bring to a boil over medium-high heat. In a small bowl, combine the cornstarch and water slowly and whisk into the boiling sauce. Simmer until sauce thickens.\nPour sauce over the rice mixture and stir to coat evenly. Pour into prepared pan and bake for 20 minutes until heated through.",
        strCategory = "Chicken",
        strArea = "Japanese",
        strTags = "Meat,Casserole",
        strYoutube = "https://www.youtube.com/watch?v=4aZr5hZXP_s",
        strSource = null,
        strImageSource = null,
        strCreativeCommonsConfirmed = null,
        dateModified = null,
        strIngredient1 = "soy sauce",
        strIngredient2 = "water",
        strIngredient3 = "brown sugar",
        strIngredient4 = "ground ginger",
        strIngredient5 = "garlic powder",
        strIngredient6 = "cornstarch",
        strIngredient7 = "chicken breasts",
        strIngredient8 = "stir-fry vegetables",
        strIngredient9 = "brown rice",
        strIngredient10 = "",
        strIngredient11 = "",
        strIngredient12 = "",
        strIngredient13 = "",
        strIngredient14 = "",
        strIngredient15 = "",
        strIngredient16 = "",
        strIngredient17 = "",
        strIngredient18 = "",
        strIngredient19 = "",
        strIngredient20 = "",
        strMeasure1 = "3/4 cup",
        strMeasure2 = "1/2 cup",
        strMeasure3 = "1/4 cup",
        strMeasure4 = "1/2 teaspoon",
        strMeasure5 = "1/2 teaspoon",
        strMeasure6 = "4 tablespoons",
        strMeasure7 = "2",
        strMeasure8 = "1 (12 oz.) [frozen]",
        strMeasure9 = "3 cups",
        strMeasure10 = "",
        strMeasure11 = "",
        strMeasure12 = "",
        strMeasure13 = "",
        strMeasure14 = "",
        strMeasure15 = "",
        strMeasure16 = "",
        strMeasure17 = "",
        strMeasure18 = "",
        strMeasure19 = "",
        strMeasure20 = ""
    )
    MealAppTheme {
        MealDetailContent(meal = mockMeal)
    }
}


package com.dev.mealapp.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dev.mealapp.features.categories.CategoriesScreen
import com.dev.mealapp.features.categories.CategoriesViewModel
import com.dev.mealapp.features.mealdetail.MealDetailScreen
import com.dev.mealapp.features.mealdetail.MealDetailViewModel
import com.dev.mealapp.features.meals.MealsScreen
import com.dev.mealapp.features.meals.MealsViewModel

sealed class Screen(val route: String) {
    object Categories : Screen("categories")
    object Meals : Screen("meals/{categoryName}") {
        fun createRoute(categoryName: String) = "meals/$categoryName"
    }

    object MealDetail : Screen("mealDetail/{mealId}") {
        fun createRoute(mealId: String) = "mealDetail/$mealId"
    }
}

@Composable
fun MealNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Categories.route
    ) {
        composable(Screen.Categories.route) {
            val categoriesViewModel: CategoriesViewModel = viewModel()
            CategoriesScreen(
                viewModel = categoriesViewModel,
                onCategoryClick = { category ->
                    navController.navigate(Screen.Meals.createRoute(category.strCategory))
                }
            )
        }

        composable(
            route = Screen.Meals.route,
            arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            val mealsViewModel: MealsViewModel = viewModel()
            MealsScreen(
                categoryName = categoryName,
                viewModel = mealsViewModel,
                onBackClick = { navController.popBackStack() },
                onMealClick = { meal ->
                    navController.navigate(Screen.MealDetail.createRoute(meal.idMeal))
                }
            )
        }

        composable(
            route = Screen.MealDetail.route,
            arguments = listOf(navArgument("mealId") { type = NavType.StringType })
        ) { backStackEntry ->
            val mealId = backStackEntry.arguments?.getString("mealId") ?: ""
            val mealDetailViewModel: MealDetailViewModel = viewModel()
            MealDetailScreen(
                mealId = mealId,
                viewModel = mealDetailViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

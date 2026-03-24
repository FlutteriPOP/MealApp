package com.dev.mealapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.mealapp.data.model.Category
import com.dev.mealapp.data.model.Meal
import com.dev.mealapp.data.model.MealDetail
import com.dev.mealapp.data.remote.recipeService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _categoriesState = MutableStateFlow(RecipeState())
    val categoriesState: StateFlow<RecipeState> = _categoriesState

    private val _mealsState = MutableStateFlow(MealsState())
    val mealsState: StateFlow<MealsState> = _mealsState

    private val _mealDetailState = MutableStateFlow(MealDetailState())
    val mealDetailState: StateFlow<MealDetailState> = _mealDetailState

    init {
        fetchCategories()
    }

    fun fetchCategories() {
        _categoriesState.value = _categoriesState.value.copy(loading = true, error = null)
        viewModelScope.launch {
            try {
                Log.d("MainViewModel", "Fetching categories...")
                val response = recipeService.getCategories()
                _categoriesState.value = _categoriesState.value.copy(
                    list = response.categories,
                    loading = false,
                    error = null
                )
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error fetching Categories", e)
                _categoriesState.value = _categoriesState.value.copy(
                    loading = false,
                    error = "Error fetching Categories: ${e.message}"
                )
            }
        }
    }

    fun fetchMealsByCategory(categoryName: String) {
        _mealsState.value = MealsState(loading = true)
        viewModelScope.launch {
            try {
                val response = recipeService.getMealsByCategory(categoryName)
                _mealsState.value = MealsState(
                    list = response.meals,
                    loading = false
                )
            } catch (e: Exception) {
                _mealsState.value = MealsState(
                    loading = false,
                    error = "Error fetching Meals: ${e.message}"
                )
            }
        }
    }

    fun fetchMealDetails(mealId: String) {
        _mealDetailState.value = MealDetailState(loading = true)
        viewModelScope.launch {
            try {
                val response = recipeService.getMealDetails(mealId)
                _mealDetailState.value = MealDetailState(
                    mealDetail = response.meals.firstOrNull(),
                    loading = false
                )
            } catch (e: Exception) {
                _mealDetailState.value = MealDetailState(
                    loading = false,
                    error = "Error fetching Meal Details: ${e.message}"
                )
            }
        }
    }

    data class RecipeState(
        val loading: Boolean = true,
        val list: List<Category> = emptyList(),
        val error: String? = null
    )

    data class MealsState(
        val loading: Boolean = false,
        val list: List<Meal> = emptyList(),
        val error: String? = null
    )

    data class MealDetailState(
        val loading: Boolean = false,
        val mealDetail: MealDetail? = null,
        val error: String? = null
    )
}

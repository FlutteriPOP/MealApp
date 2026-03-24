package com.dev.mealapp.features.meals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.mealapp.data.model.Meal
import com.dev.mealapp.data.remote.recipeService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MealsViewModel : ViewModel() {

    private val _mealsState = MutableStateFlow(MealsState())
    val mealsState: StateFlow<MealsState> = _mealsState

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

    data class MealsState(
        val loading: Boolean = false,
        val list: List<Meal>? = emptyList(),
        val error: String? = null
    )
}

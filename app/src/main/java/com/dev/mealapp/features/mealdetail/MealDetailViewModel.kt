package com.dev.mealapp.features.mealdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.mealapp.data.model.MealDetail
import com.dev.mealapp.data.remote.recipeService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MealDetailViewModel : ViewModel() {

    private val _mealDetailState = MutableStateFlow(MealDetailState())
    val mealDetailState: StateFlow<MealDetailState> = _mealDetailState

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

    data class MealDetailState(
        val loading: Boolean = false,
        val mealDetail: MealDetail? = null,
        val error: String? = null
    )
}

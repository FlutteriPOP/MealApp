package com.dev.mealapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel(){

    private val _categoriesState = MutableStateFlow(RecipeState())
    val categoriesState: StateFlow<RecipeState> = _categoriesState

init {
  fetchCategories()
}

    private fun fetchCategories() {
        viewModelScope.launch {
          try {
            val response = recipeService.getCategories()
            _categoriesState.value = _categoriesState.value.copy(
                list = response.categories,
                loading = false,
                error = null
            )
          }catch (e: Exception){
            _categoriesState.value = _categoriesState.value.copy(
                loading = false,
                error = "Error fetching Categories ${e.message}"
            )
          }
        }
    }

    data class RecipeState(
        val loading: Boolean = true,
        val list: List<Category> = emptyList(),
        val error: String? = null
    )
}


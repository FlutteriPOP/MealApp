package com.dev.mealapp.features.categories

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.mealapp.data.model.Category
import com.dev.mealapp.data.remote.recipeService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoriesViewModel : ViewModel() {

    private val _categoriesState = MutableStateFlow(CategoriesState())
    val categoriesState: StateFlow<CategoriesState> = _categoriesState

    init {
        fetchCategories()
    }

    fun fetchCategories() {
        _categoriesState.value = _categoriesState.value.copy(loading = true, error = null)
        viewModelScope.launch {
            try {
                Log.d("CategoriesViewModel", "Fetching categories...")
                val response = recipeService.getCategories()
                _categoriesState.value = _categoriesState.value.copy(
                    list = response.categories,
                    loading = false,
                    error = null
                )
            } catch (e: Exception) {
                Log.e("CategoriesViewModel", "Error fetching Categories", e)
                _categoriesState.value = _categoriesState.value.copy(
                    loading = false,
                    error = "Error fetching Categories: ${e.message}"
                )
            }
        }
    }

    data class CategoriesState(
        val loading: Boolean = true,
        val list: List<Category> = emptyList(),
        val error: String? = null
    )
}

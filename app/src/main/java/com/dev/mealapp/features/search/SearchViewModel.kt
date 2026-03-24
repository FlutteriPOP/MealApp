package com.dev.mealapp.features.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.mealapp.data.model.Meal
import com.dev.mealapp.data.remote.recipeService
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchViewModel : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchState = MutableStateFlow(SearchState())
    val searchState: StateFlow<SearchState> = _searchState

    init {
        _searchQuery
            .debounce(500)
            .distinctUntilChanged()
            .onEach { query ->
                if (query.isBlank()) {
                    _searchState.value = SearchState()
                } else {
                    performSearch(query)
                }
            }
            .launchIn(viewModelScope)
    }

    fun onQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    private fun performSearch(query: String) {
        _searchState.value = _searchState.value.copy(loading = true, error = null)
        viewModelScope.launch {
            try {
                val response = recipeService.searchMeals(query)
                _searchState.value = SearchState(
                    list = response.meals ?: emptyList(),
                    loading = false
                )
            } catch (e: Exception) {
                _searchState.value = SearchState(
                    loading = false,
                    error = "Search failed: ${e.message}"
                )
            }
        }
    }

    data class SearchState(
        val loading: Boolean = false,
        val list: List<Meal> = emptyList(),
        val error: String? = null
    )
}

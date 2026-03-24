package com.dev.mealapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String,
    val strCategoryDescription: String
) : Parcelable

data class CategoriesResponse(
    val categories: List<Category>
)

data class Meal(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String
)

data class MealsResponse(
    val meals: List<Meal>
)

data class MealDetail(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String,
    val strInstructions: String,
    val strCategory: String,
    val strArea: String,
    val strYoutube: String?,
    val strIngredient1: String?,
    val strIngredient2: String?,
    val strIngredient3: String?,
    val strIngredient4: String?,
    val strIngredient5: String?,
    val strIngredient6: String?,
    val strIngredient7: String?,
    val strIngredient8: String?,
    val strIngredient9: String?,
    val strIngredient10: String?,
    val strMeasure1: String?,
    val strMeasure2: String?,
    val strMeasure3: String?,
    val strMeasure4: String?,
    val strMeasure5: String?,
    val strMeasure6: String?,
    val strMeasure7: String?,
    val strMeasure8: String?,
    val strMeasure9: String?,
    val strMeasure10: String?
)

data class MealDetailResponse(
    val meals: List<MealDetail>
)

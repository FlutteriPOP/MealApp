package com.dev.mealapp.data.remote

import com.dev.mealapp.data.model.CategoriesResponse
import com.dev.mealapp.data.model.MealDetailResponse
import com.dev.mealapp.data.model.MealsResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val logging = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private val client = OkHttpClient.Builder()
    .addInterceptor(logging)
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl("https://www.themealdb.com/api/json/v1/1/")
    .addConverterFactory(GsonConverterFactory.create())
    .client(client)
    .build()

interface ApiServices {
    @GET("categories.php")
    suspend fun getCategories(): CategoriesResponse

    @GET("filter.php")
    suspend fun getMealsByCategory(@Query("c") categoryName: String): MealsResponse

    @GET("lookup.php")
    suspend fun getMealDetails(@Query("i") mealId: String): MealDetailResponse
}

val recipeService: ApiServices = retrofit.create(ApiServices::class.java)

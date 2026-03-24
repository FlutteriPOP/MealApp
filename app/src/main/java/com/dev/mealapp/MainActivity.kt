package com.dev.mealapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.dev.mealapp.navigation.MealNavHost
import com.dev.mealapp.ui.theme.MealAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MealAppTheme {
                MealApp()
            }
        }
    }
}

@Composable
fun MealApp() {
    val navController = rememberNavController()
    MealNavHost(navController = navController)
}

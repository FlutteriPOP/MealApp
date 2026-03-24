# MealApp 🍲

A modern Android application built with Jetpack Compose that allows users to explore various meal categories and discover delicious recipes.

## Features ✨

- **Explore Categories:** Browse through a wide variety of meal categories (Beef, Chicken, Vegetarian, etc.).
- **Meal Listings:** View all meals associated with a specific category.
- **Detailed Recipes:** Get step-by-step instructions and ingredients for each meal.
- **YouTube Integration:** Watch recipe videos directly within the app.
- **Modern UI:** Built entirely with Jetpack Compose, featuring smooth animations and a clean Material 3 design.

## Tech Stack 🛠️

- **Language:** [Kotlin](https://kotlinlang.org/)
- **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Networking:** [Retrofit](https://square.github.io/retrofit/) with [Gson](https://github.com/google/gson) for API communication.
- **Image Loading:** [Coil](https://coil-kt.github.io/coil/) for asynchronous image loading.
- **Navigation:** [Compose Navigation](https://developer.android.com/jetpack/compose/navigation) for seamless screen transitions.
- **Video Player:** [Android YouTube Player](https://github.com/PierfrancescoSoffritti/android-youtube-player) for recipe videos.
- **Architecture:** MVVM (Model-View-ViewModel) with Kotlin Coroutines and StateFlow.
- **App Size Optimization:** R8/ProGuard enabled for minification and resource shrinking.

## Getting Started 🚀

1.  Clone the repository.
2.  Open the project in **Android Studio (Ladybug or newer)**.
3.  Sync the Gradle files.
4.  Run the app on an emulator or a physical device.

## API Reference 🔗

This app uses [TheMealDB API](https://www.themealdb.com/api.php) to fetch its data.

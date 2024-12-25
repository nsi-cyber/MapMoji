# BarBlend

BarBlend is a mobile application that allows you to discover cocktail recipes, save your favorite cocktails, and explore new ones. The app fetches cocktail data via various API calls and stores your favorite and recent cocktails in a local database.


<p align="center">
  <img src="https://github.com/user-attachments/assets/264e42ce-0912-4350-95db-8f3b0975f4dc" width="200" />
  <img src="https://github.com/user-attachments/assets/80345cb8-0b29-4d5a-9e96-7c9dd795c4b3" width="200" />
  <img src="https://github.com/user-attachments/assets/ab299e14-990f-482e-a6fb-947ccd75301b" width="200" />
  
  <img src="https://github.com/user-attachments/assets/5b2063e5-33bd-4e52-ad34-e5df3c7d79f2" width="200" />
  <img src="https://github.com/user-attachments/assets/5e9668b3-6062-495f-97df-a0605a6647a3" width="200" />
  <img src="https://github.com/user-attachments/assets/b1c07f2e-1ce9-478c-86be-de1b941b32da" width="200" />
  
  <img src="https://github.com/user-attachments/assets/831c4146-be21-4052-998e-2a0a1af7bed0" width="200" />
</p>

## Features

- **Cocktail Details**: View the recipe and ingredients of selected cocktails.
- **Random Cocktails**: Get random cocktail suggestions.
- **Popular Cocktails**: Discover the most popular cocktails.
- **Latest Cocktails**: View newly added cocktails.
- **Favorites**: Save and manage your favorite cocktails.
- **Recent Cocktails**: Keep track of your recently viewed cocktails.
- **Notifications**: Receive notifications about new and popular cocktails.
- **Background Work**: Perform background tasks such as fetching new data using Workers.
- **Offline Mode**: When no internet connection is available, only the Favorites page is accessible.

## Technologies Used

### Android

- **Kotlin**: Used as the programming language for the app.
- **Android Jetpack**: Modern Android development components:
  - **MutableState**: For observable data holders.
  - **ViewModel**: For UI-related data that survives configuration changes.
  - **Room**: For local database management.
  - **Navigation**: For handling navigation within the app.
  - **WorkManager**: For scheduling background tasks.
  - **DataStore**: For managing key-value data and replacing SharedPreferences.
- **Retrofit**: For making API calls.
- **Coroutines**: For asynchronous programming.
- **Kotlin Flow**: For managing asynchronous data streams.
- **Dagger-Hilt**: For dependency injection.
- **Glide**: For image loading and caching.
- **Notification**: For sending user notifications.
- **ConnectivityManager**: For actively checking internet connectivity status.
- **Lottie**: For beautiful animations and enhancing the user experience.

## API

- **TheCocktailDB API**: Used to fetch cocktail data.

## Detailed Description

### Architecture

The app follows the MVVM (Model-View-ViewModel) architecture pattern along with Clean Architecture principles, ensuring a separation of concerns and making the code more maintainable, scalable, and testable.

### Data Fetching

- **Retrofit**: Used to define the API endpoints and make network requests.
- **Coroutines and Flow**: Used to handle asynchronous operations and manage data streams.

### Local Storage

- **Room Database**: Used to store favorite and recent cocktails locally.
- **DataStore**: Used for storing user preferences and settings in a more modern and efficient way compared to SharedPreferences. DataStore is used in conjunction with WorkManager to ensure that background tasks respect user preferences.

### Background Tasks

- **WorkManager**: Used for scheduling and managing background tasks such as fetching new cocktail data periodically.
- **Worker**: Defined tasks that run in the background, ensuring they complete even if the app is closed.

### Notifications

- **Notification Manager**: Used to send notifications to the user about new and popular cocktails.


### To Use...

- **IMPORTANT**: You need to get api key from [rapidapi coc](https://rapidapi.com/thecocktaildb/api/the-cocktail-db/) to use app and add it into libnative-lib.cpp file in app/cpp.


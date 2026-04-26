# WeatherRoute — Android

An Android app that overlays real-time and forecast weather conditions along a planned route. Enter your origin, destination, and any stops, set your departure time, and see weather conditions mapped across your entire journey.

## Features

- Route planning with origin, destination, and up to 25 intermediate stops
- Real-time weather overlay along the route at sampled intervals
- Departure time scheduling with date and time pickers
- Travel mode selection — driving, walking, cycling, transit
- Weather forecasts up to 10 days in advance
- Google Maps integration with floating card UI

## Tech Stack

- **Language:** Java
- **Architecture:** MVVM (ViewModel + LiveData + Repository)
- **Maps & Routing:** Google Maps SDK, Google Routes API
- **Weather:** Google Weather API
- **Networking:** Retrofit 2 + OkHttp + Gson
- **Local Storage:** Room (planned)
- **Background Jobs:** WorkManager (planned)
- **Image Loading:** Glide

## Architecture

```
UI (MainActivity)
    ↕ LiveData
ViewModel (RouteViewModel, WeatherViewModel)
    ↕ Callbacks
Repository (RouteRepository, WeatherRepository)
    ↕ Retrofit
Google Routes API / Google Weather API
```

## Setup

1. Clone the repository
2. Get API keys from [Google Cloud Console](https://console.cloud.google.com):
   - Maps SDK for Android
   - Routes API
   - Places API (Legacy)
   - Weather API
3. Create a `local.properties` file in the project root and add:
```
GOOGLE_MAPS_API_KEY=your_api_key_here
```
4. Build and run in Android Studio

> API keys are never committed to the repository. You must supply your own.

## Status

Active development! Core networking and data models complete. UI layout/functionality, places autocomplete, route drawing, and weather marker overlay in progress.

All rights reserved

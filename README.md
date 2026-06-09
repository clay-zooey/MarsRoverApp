# Mars Vista Rover App

An Android application built with Jetpack Compose, Kotlin Coroutines, Retrofit, and Hilt for browsing Mars Rover photographs via the Mars Vista API.

<img width="350" height="800" alt="Screenshot_20260609_145211" src="https://github.com/user-attachments/assets/475d98aa-b11c-44f0-a3b5-52efae4baf3f" /> <img width="350" height="800" alt="Screenshot_20260609_145242" src="https://github.com/user-attachments/assets/4678eaa6-4439-40e1-ac03-9acc91a1feb3" />



## Features & Optimizations

* **Paginated Photo Loading (Infinite Scroll)**: Defaults to today's date and fetches photographs dynamically in pages of 50 as the user scrolls.
* **Unified Error Handling**: Exception handling is decoupled into a clean `AppError` sealed model that resolves dynamically to localized strings inside Composables.
* **API Key Interceptor**: Validates key existence centrally in an OkHttp Interceptor.
* **Responsive Column Layout**: The Rovers list grid automatically adapts to **1 column in portrait** and **2 columns in landscape**.

---

## Configuration

Add your Mars Vista API key to the project root `local.properties` file (ignored by Git):

```properties
MARS_VISTA_API_KEY=your_api_key_here
```

---

## Commands

### Build Application
```bash
./gradlew assembleDebug
```

### Run Unit Tests
To execute the repository caching, exception mapping, and image-fallback test suites:
```bash
./gradlew testDebugUnitTest
```

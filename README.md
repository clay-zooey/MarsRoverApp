# Mars Rover App

Android take-home app for browsing Mars Rover photos from the Mars Vista API.

## API Key

Keep the Mars Vista API key out of Git by adding it to the project root `local.properties` file:

```properties
MARS_VISTA_API_KEY=mv_live_your_key_here
```

`local.properties` is ignored by Git. Gradle exposes this value to app code through `BuildConfig.MARS_VISTA_API_KEY`. The Mars Vista base URL is also generated as `BuildConfig.MARS_VISTA_BASE_URL`.

If the value is missing, the app builds successfully but the rover screen shows a retryable configuration error instead of making an unauthenticated request.

## Build

```bash
./gradlew assembleDebug
```

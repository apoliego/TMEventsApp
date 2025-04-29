# TM Events App

The TM Events App is an Android application that allows users to browse events such as concerts and games. It fetches event data from a remote API, caches it locally for offline access, and use a responsive UI built with Jetpack Compose. The app follows **Clean Architecture** principles and is structured as a multi-module project for scalability and maintainability.

## Features

- **Browse Events**: View a list of events with details like name, date and image.
- **Offline Support**: Cache events locally using Room for offline access.
- **Real-Time Data**: Fetch the latest events from a remote API using Retrofit.
- **Optional Filtering**: Search events by name.
- **Error Handling**: Display user-friendly messages for network or server errors.
- **Unit Testing**: Comprehensive unit tests for repository and ViewModel logic.

## Architecture

The app follows **Clean Architecture** to ensure separation of concerns, testability, and scalability. It is divided into three modules:

- **:app**: Contains the UI layer (Jetpack Compose screens, ViewModels) and Hilt dependency injection setup.
- **:data**: Implements data sources (remote via Retrofit, local via Room) and repositories.
- **:domain**: Defines use cases, domain models, and repository interfaces.

The data flow is as follows:
1. The UI layer (`:app`) interacts with use cases (`:domain`).
2. Use cases call repository implementations (`:data`).
3. Repositories fetch data from remote (API) or local (Room) sources, prioritizing remote data and falling back to local on errors.

## Tech Stack

- **Language**: Kotlin 2.1.0
- **UI**: Jetpack Compose 1.7.4
- **Dependency Injection**: Hilt 2.51.1
- **Networking**: Retrofit 2.11.0 with Gson converter
- **Local Database**: Room 2.6.1
- **Coroutines**: Kotlinx Coroutines 1.9.0
- **ViewModel**: Lifecycle ViewModel 2.8.6
- **Image Loading**: Coil 2.7.0
- **Testing**:
  - JUnit 4.13.2
  - MockK 1.13.13
  - Kotlinx Coroutines Test 1.9.0
  - AndroidX Core Testing 2.2.0

## Setup

### Prerequisites

- **Android Studio**: Koala Feature Drop | 2024.1.2 or later
- **JDK**: 17
- **Git**: To clone the repository

### Steps

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/apoliego/TMEventsApp.git
   cd TMEventsApp
   ```

2. **Open in Android Studio**:
   - Open Android Studio and select `Open an existing project`.
   - Navigate to the cloned repository folder and click `OK`.

3. **Sync Project with Gradle**:
   - Click `Sync Project with Gradle Files` in Android Studio, or run:
     ```bash
     ./gradlew build
     ```

4. **Install Dependencies**:
   - Ensure all dependencies in `build.gradle` files (`:app`, `:data`, `:domain`) are resolved.
   - Key dependencies are listed in the **Tech Stack** section.

## Running the App

1. **Set Up an Emulator or Device**:
   - Configure an Android emulator (API 30 or higher) in Android Studio, or connect a physical device with USB debugging enabled.

2. **Run the App**:
   - Select the `app` configuration in Android Studio.
   - Click `Run` or use:
     ```bash
     ./gradlew :app:installDebug
     ```
   - The app will launch on the emulator or device, displaying the event list.

3. **Troubleshooting**:
   - **Build Errors**: Ensure JDK 17 is selected (`File > Project Structure > SDK Location > Gradle Settings`).
   - **Network Issues**: Verify internet connectivity.
   - **UI Issues**: Ensure Jetpack Compose is supported on the emulator (API 21+).

## Testing

The project includes unit tests for the `:data` (repository) and `:app` (ViewModel) modules.

### Running Tests

1. **Run All Tests**:
   - For the entire project:
     ```bash
     ./gradlew test
     ```
   - For specific modules:
     ```bash
     ./gradlew :app:test
     ./gradlew :data:test
     ```

2. **Run Specific Tests**:
   - Example for `EventsViewModelTest`:
     ```bash
     ./gradlew :app:test --tests "com.ticketmaster.eventsapp.ui.event.EventViewModelTest"
     ```
   - In Android Studio, right-click a test class (e.g., `EventsViewModelTest.kt`) and select `Run`.

3. **Test Coverage**:
   - Tests cover:
     - `EventsRepositoryImpl`: Remote data fetching, local caching, error handling, and timeouts.
     - `EventsViewModel`: Initial state, data loading, error handling, and optional filtering.
   - View coverage reports in `app/build/reports/tests/testDebugUnitTest/index.html`.

4. **Dependencies**:
   - Ensure test dependencies (`JUnit`, `MockK`, `Kotlinx Coroutines Test`, `AndroidX Core Testing`) are included in `build.gradle` files.

---

**Contact**: For questions or support, open an issue on GitHub or contact [apoliego@gmail.com](mailto:apoliego@gmail.com).

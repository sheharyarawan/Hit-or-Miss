# HitOrMiss – Cricket Stats Comparison Game

HitOrMiss is an Android application that tests your cricket knowledge through quick head-to-head player comparisons.<br>
Pick the format, choose a stat category, and decide which international cricketer has the better numbers.

---

## Features

- Choose match format: **Test**, **ODI**, or **T20I**
- Six stat categories: Runs, Sixes, Fours, Wickets, Strike Rate, Economy
- Random player matchups from a curated international player list
- Format-specific stats (Test / ODI / T20I values)
- Two lives per round — game ends after 2 wrong answers
- Result screen with accuracy and XP (+10 per correct answer)
- Player data fetched from **CricAPI** and cached locally with Room
- Splash screen on app launch
- Clean UI with Material Design and custom branding

---

## Tech Stack

- Kotlin (Android)
- Room Database
- Retrofit + Gson
- ViewModel + LiveData
- Navigation Component
- Coroutines
- View Binding
- Material Design Components
- XML Layouts

---

## Architecture

- MVVM-inspired structure (ViewModel + Repository + DAO)
- Room for offline player and stats storage
- Retrofit for CricAPI network calls
- `GenerateQuestionUseCase` and `GameEngine` for game logic
- Separation of UI, domain, and data layers

---

## App Flow

```
Splash → Choose Format → Home (Stat Category) → Game → Result
```

---

## API Setup

This app uses [CricAPI](https://www.cricapi.com/). You need an API key before running.

1. Create a file named `local.properties` in the project root (if it does not exist).
2. Add your API key:

```properties
CRICKET_API_KEY=your_api_key_here
```

3. Rebuild the project in Android Studio so `BuildConfig` picks up the key.

> `local.properties` is ignored by Git — do not commit your API key.

---

## Screenshots

> Add your screenshots inside a `screenshots/` folder in the repo root.

### Splash Screen
![Splash Screen](screenshots/splash.png)

---

### Choose Format
![Choose Format](screenshots/format.png)

---

### Home Screen
![Home Screen](screenshots/home.png)

---

### Game Screen
![Game Screen](screenshots/game.png)

---

### Result Screen
![Result Screen](screenshots/result.png)

---

## How to Run Project

1. Clone this repository

```bash
git clone https://github.com/yourusername/HitOrMiss.git
```

2. Open the project in Android Studio  
   Launch Android Studio → **Open** → select the cloned `HitOrMiss` folder

3. Add your CricAPI key in `local.properties` (see [API Setup](#api-setup))

4. Let Gradle sync  
   Wait for dependencies to download  
   If prompted, click **Sync Now**

5. Set up SDK (if required)  
   **File → Settings → Android SDK** → install required SDK platforms and build tools

6. Run the app  
   Connect a device or start an emulator → click **Run ▶**

---

## Requirements

- Android Studio (latest stable recommended)
- Min SDK: 24
- Target SDK: 35
- Internet connection (for first-time player data sync)

---

## Project Structure

```
app/src/main/java/com/example/hitormiss/
├── data/          # API, Room, Repository, ViewModel
├── domain/        # Use cases
├── ui/            # Fragments (Format, Home, Game, Result)
├── utils/         # Game engine, stats helpers, dependencies
└── SplashActivity.kt
```

---

## License

This project is for educational purposes. Player data is provided by CricAPI.

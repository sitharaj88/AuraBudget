# 💰 AuraBudget

<div align="center">
  
  ![AuraBudget Logo](app/src/main/res/drawable/aura_logo.xml)
  
  **A Beautiful & Intelligent Personal Finance Management App**
  
  [![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com/)
  [![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org/)
  [![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-orange.svg)](https://developer.android.com/jetpack/compose)
  [![Material 3](https://img.shields.io/badge/Design-Material%203-purple.svg)](https://m3.material.io/)
  [![License](https://img.shields.io/badge/License-MIT-red.svg)](LICENSE)

</div>

## ✨ Features

### 🎯 Core Features
- **💸 Transaction Management** - Track income and expenses with ease
- **📊 Budget Planning** - Set and monitor monthly budgets
- **📈 Analytics & Insights** - Visual charts and spending analysis
- **🎯 Financial Goals** - Set and track your savings goals
- **📱 Modern UI** - Beautiful Material 3 design with dark/light themes
- **🔒 Secure Storage** - Local data storage with Room database

### 🚀 Advanced Features
- **📊 Interactive Charts** - MPAndroidChart integration for data visualization
- **🏷️ Category Management** - Organize transactions by custom categories
- **⚙️ Settings & Preferences** - Customizable app experience
- **🎨 Dynamic Theming** - Material You color scheme support
- **📱 Responsive Design** - Optimized for all screen sizes

## 🏗️ Architecture

AuraBudget follows modern Android development best practices with **Clean Architecture** and **MVVM** pattern:

```
📁 app/src/main/java/in/sitharaj/aurabudget/
├── 📁 data/           # Data layer (Repository, Database, DataStore)
├── 📁 di/             # Dependency Injection (Hilt modules)
├── 📁 domain/         # Business logic (Models, Repository interfaces)
├── 📁 presentation/   # UI layer (Screens, ViewModels, Components)
└── 📁 ui/             # Theme, Typography, Design system
```

## 🛠️ Tech Stack

### Core Technologies
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Architecture:** MVVM + Clean Architecture
- **Dependency Injection:** Hilt

### Libraries & Dependencies
- **🗄️ Database:** Room (Local storage)
- **💾 Preferences:** DataStore
- **🧭 Navigation:** Navigation Compose
- **📊 Charts:** MPAndroidChart
- **🎨 UI:** Material 3, Material Icons Extended
- **⚡ Async:** Coroutines & Flow
- **📅 Date/Time:** Kotlinx DateTime
- **🔐 Permissions:** Accompanist Permissions

## 📱 Screenshots

| Dashboard | Analytics | Budget Planning | Goals |
|-----------|-----------|-----------------|--------|
| ![Dashboard](screenshots/dashboard.png) | ![Analytics](screenshots/analytics.png) | ![Budget](screenshots/budget.png) | ![Goals](screenshots/goals.png) |

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog | 2023.1.1 or newer
- JDK 11 or higher
- Android SDK with API level 27+ (Android 8.1)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/sitharaj88/AuraBudget.git
   cd AuraBudget
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory and select it

3. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   ```
   Or simply click the "Run" button in Android Studio

### 🔧 Configuration

The app uses the following configuration:
- **Min SDK:** API 27 (Android 8.1)
- **Target SDK:** API 36 (Android 14)
- **Compile SDK:** API 36

## 📂 Project Structure

```
AuraBudget/
├── 📁 app/
│   ├── 📁 src/main/
│   │   ├── 📁 java/in/sitharaj/aurabudget/
│   │   │   ├── 📁 data/
│   │   │   │   ├── 📁 database/     # Room entities, DAOs
│   │   │   │   ├── 📁 datastore/    # DataStore preferences
│   │   │   │   └── 📁 repository/   # Repository implementations
│   │   │   ├── 📁 di/               # Hilt dependency injection
│   │   │   ├── 📁 domain/
│   │   │   │   ├── 📁 model/        # Data models
│   │   │   │   ├── 📁 repository/   # Repository interfaces
│   │   │   │   └── 📁 usecase/      # Business logic use cases
│   │   │   ├── 📁 presentation/
│   │   │   │   ├── 📁 components/   # Reusable UI components
│   │   │   │   ├── 📁 navigation/   # Navigation setup
│   │   │   │   ├── 📁 screen/       # App screens
│   │   │   │   └── 📁 viewmodel/    # ViewModels
│   │   │   └── 📁 ui/
│   │   │       ├── 📁 theme/        # Material 3 theming
│   │   │       └── Color.kt, Typography.kt
│   │   ├── 📁 res/
│   │   │   ├── 📁 drawable/         # Vector drawables, icons
│   │   │   ├── 📁 mipmap/           # App icons
│   │   │   └── 📁 values/           # Strings, colors, themes
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── 📁 gradle/
│   └── libs.versions.toml           # Version catalog
├── build.gradle.kts
└── README.md
```

## 🎨 Design System

AuraBudget implements a comprehensive design system based on Material 3:

### Color Palette
- **Primary:** Gradient from #667EEA to #6B73FF
- **Surface:** Dynamic Material You colors
- **Theme Support:** Light and Dark modes

### Typography
- **Headings:** Custom typography scale
- **Body Text:** Optimized for readability
- **Monospace:** For financial figures

## 🧪 Testing

Run the test suite:

```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest
```

## 📦 Build Variants

- **Debug:** Development build with debugging enabled
- **Release:** Production build with optimizations

```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease
```

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Style
- Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful commit messages
- Write tests for new features
- Update documentation as needed

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Sitharaj**
- GitHub: [@sitharaj88](https://github.com/sitharaj88)
- Email: sitharaj.info@gmail.com

## 🙏 Acknowledgments

- [Material Design 3](https://m3.material.io/) for the design system
- [Jetpack Compose](https://developer.android.com/jetpack/compose) team for the amazing UI toolkit
- [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) for chart visualizations
- Android development community for inspiration and support

## 📈 Roadmap

### Upcoming Features
- [ ] 🌐 Cloud synchronization
- [ ] 💳 Bank account integration
- [ ] 📊 Advanced reporting
- [ ] 🔔 Smart notifications
- [ ] 🤖 AI-powered insights
- [ ] 🌍 Multi-currency support
- [ ] 📤 Export functionality
- [ ] 👥 Family budget sharing

### Version History
- **v1.0.0** - Initial release with core features

---

<div align="center">
  
  **Made with ❤️ and ☕ in India**
  
  If you found this project helpful, please give it a ⭐!
  
</div>

# 🚌 PrimeBus - Modern Bus Booking Android Application

PrimeBus is a modern Android bus booking application built using **Kotlin**, **Jetpack Compose**, **MVVM Architecture**, **Firebase**, **Room Database**, and **Hilt Dependency Injection**.

The application provides a complete bus reservation experience with real-time seat booking, offline support, booking history, PDF ticket generation, and a modern Material 3 user interface.

![Platform](https://img.shields.io/badge/Platform-Android-green)
![Kotlin](https://img.shields.io/badge/Kotlin-2.x-purple)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Latest-blue)
![Firebase](https://img.shields.io/badge/Firebase-Realtime%20Database-orange)
![Architecture](https://img.shields.io/badge/Architecture-MVVM-red)

---

# ✨ Features

## 🔐 Authentication

* Firebase Authentication
* User Registration & Login
* Persistent User Sessions
* Secure User Management

## 🏠 Home Screen

* Modern Material 3 UI
* Jetpack Compose-based interface
* Dynamic bus listings
* Popular & Recommended buses

## 🔍 Bus Search

* Search buses by route and date
* Dynamic bus details
* Journey planning experience

## 💺 Real-Time Seat Selection

* Dynamic seat layout rendering
* Real-time seat availability updates
* Firebase Transaction-based seat locking
* Double-booking prevention
* Live UI updates

## 👥 Passenger Details

* Passenger information collection
* Form validation
* Booking data management

## 💳 Booking Flow

* Booking summary
* Fare calculation
* Payment simulation
* Booking confirmation

## 🎫 Ticket Management

* PDF ticket generation
* Download tickets to device storage
* Booking history tracking
* Ticket viewing support

## 📡 Offline Support

* Room Database caching
* Offline-first data loading
* Pull-to-refresh synchronization
* Internet connectivity monitoring
* Automatic cache fallback

## 🆘 Help Center

* Frequently Asked Questions (FAQs)
* Support categories
* User assistance section

---

# 🏗 Architecture

PrimeBus follows the **MVVM (Model-View-ViewModel)** architecture pattern combined with the **Repository Pattern** for better scalability, maintainability, and separation of concerns.

```text
Presentation Layer
│
├── Jetpack Compose Screens
├── Reusable Components
├── Navigation
└── ViewModels

Data Layer
│
├── Repository
├── Firebase Realtime Database
├── Firebase Authentication
└── Room Database
```

### Architecture Components

* MVVM Architecture
* Repository Pattern
* Hilt Dependency Injection
* Kotlin Coroutines
* StateFlow & SharedFlow
* Shared ViewModel
* Offline-First Data Loading

---

# 🔄 Data Flow

```text
UI
 ↓
ViewModel
 ↓
Repository
 ↓
Room Cache
 ↓
Firebase Realtime Database
```

### Seat Booking Flow

```text
User Selects Seat
       ↓
Firebase Transaction
       ↓
Seat Locked
       ↓
Booking Created
       ↓
Room Cache Updated
       ↓
UI Updated
```

---

# 🛠 Tech Stack

| Category             | Technology                 |
| -------------------- | -------------------------- |
| Language             | Kotlin                     |
| UI Toolkit           | Jetpack Compose            |
| Design System        | Material 3                 |
| Architecture         | MVVM                       |
| Dependency Injection | Hilt                       |
| Local Database       | Room                       |
| Backend Database     | Firebase Realtime Database |
| Authentication       | Firebase Authentication    |
| Concurrency          | Kotlin Coroutines          |
| State Management     | StateFlow                  |
| Navigation           | Navigation Compose         |
| PDF Generation       | Android PDF APIs           |
| Offline Support      | Room + Repository Pattern  |

---

# 📂 Project Structure

```text
com.primebus
│
├── data
│   ├── models
│   ├── repository
│   ├── local
│   └── remote
│
├── ui
│   ├── screens
│   ├── components
│   ├── navigation
│   └── theme
│
├── viewmodel
│
├── di
│
├── utils
│
└── MainActivity
```

---

# ⚡ Technical Highlights

* Built entirely using Jetpack Compose
* Modern Material 3 Design System
* MVVM Architecture implementation
* Repository Pattern
* Hilt Dependency Injection
* Firebase Realtime Database integration
* Real-time seat locking using Firebase Transactions
* Double-booking prevention mechanism
* Room-based offline caching
* Offline-first architecture
* Kotlin Coroutines & StateFlow
* Shared ViewModel implementation
* Pull-to-refresh synchronization
* Connectivity-aware UI updates
* PDF ticket generation
* Dynamic seat rendering system

---

# 📱 Application Screens

* Splash Screen
* Login & Registration
* Home Screen
* Bus Search
* Bus Details
* Seat Selection
* Passenger Details
* Booking Summary
* Payment Screen
* My Trips
* Profile Screen
* Help Center

---

# 📸 Screenshots

> Add screenshots after uploading them to the repository.

| Home Screen | Seat Selection | My Trips   |
| ----------- | -------------- | ---------- |
| Screenshot  | Screenshot     | Screenshot |

| Ticket PDF | Profile    | Help Center |
| ---------- | ---------- | ----------- |
| Screenshot | Screenshot | Screenshot  |

---

# 🚀 Getting Started

## Prerequisites

* Android Studio
* Kotlin
* Firebase Project
* Google Services Configuration

## Installation

### 1. Clone Repository

```bash
git clone https://github.com/yourusername/PrimeBus.git
```

### 2. Open in Android Studio

Open the project using Android Studio.

### 3. Configure Firebase

Add your `google-services.json` file inside:

```text
app/google-services.json
```

### 4. Sync Gradle

Allow Android Studio to download all required dependencies.

### 5. Run Application

Build and run the application on an emulator or physical device.

---

# 🔮 Future Enhancements

* Online Payment Gateway Integration
* Ticket Cancellation & Refund System
* Push Notifications
* Live Bus Tracking
* Coupon & Offers Module
* Bus Rating & Reviews
* Multi-language Support
* Dark Mode Support
* Advanced Bus Filters

---

# 👨‍💻 Developer

**Daksh Singh**

BCA Student | Android Developer

* Passionate about Android Development
* Building scalable mobile applications using Kotlin & Jetpack Compose
* Interested in modern Android Architecture and Firebase-based applications

---

## ⭐ Support

If you found this project useful, consider giving it a **Star ⭐** on GitHub.

Feedback, suggestions, and contributions are always welcome.

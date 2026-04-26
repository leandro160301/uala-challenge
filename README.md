# Cities App – Technical Overview

##  Overview

This project implements a city search application capable of handling a dataset of ~200k cities efficiently, while providing a smooth and responsive user experience.

The focus of the solution is on:

* Fast search performance
* Clean architecture
* Reactive UI
* Good UX decisions

---

##  Search Strategy

The list of cities is **pre-sorted alphabetically** (by normalized name and country).
This allows us to avoid scanning the entire list on every search.

Instead, we use a technique based on **binary search (`lowerBound`)**:

1. We find the **first position** where the prefix could appear (`O(log n)`)
2. From there, we iterate forward while elements match the prefix (`O(k)`)

 This results in a complexity of:

```
O(log n + k)
```

Compared to a naive linear search (`O(n)`), this is significantly faster for large datasets.

This is why the search remains very responsive even with ~200k entries.

---

##  Normalization

To ensure consistent and user-friendly search behavior, we normalize both:

* City names (once, during mapping)
* User input (on each search)

Normalization includes:

* Lowercasing text
* Removing accents/diacritics

This allows searches like:

* `"sao"` → matches `"São Paulo"`
* `"berlin"` → matches `"Berlin"`

---

##  Architecture

The project follows a **clean architecture approach**, divided into:

* **Data layer**

  * Remote data source (network)
  * Local data source (favorites persistence)
  * Repository implementation

* **Domain layer**

  * Business models (`City`)
  * Use cases (`GetCities`, `SearchCities`, `ToggleFavorite`)
  * Search logic (prefix + binary search)

* **Presentation layer**

  * ViewModel (state management)
  * UI (Jetpack Compose)
  * Screen state & navigation handling

This separation improves:

* Maintainability
* Testability
* Readability

---

##  Networking Decision

The app uses `HttpURLConnection` instead of libraries like Retrofit.

Reason:

* The challenge explicitly restricts third-party libraries
* `HttpURLConnection` is part of the standard Android SDK
* It is sufficient for this use case (simple GET request)

---

##  Persistence (Favorites)

Favorites are stored using **DataStore (Preferences)**.

Reason for choosing DataStore over SharedPreferences:

* Asynchronous and coroutine-friendly
* Safer (no blocking calls)
* Better suited for reactive flows

Favorites are exposed as a `Flow`, allowing the UI to update automatically when they change.

---

##  Performance Decisions

### In-memory caching

The city list is:

* Loaded once
* Stored in memory
* Reused for all searches

This avoids:

* Re-fetching data
* Re-processing the list repeatedly

Trade-off:

* Slightly higher memory usage
* Much faster search and filtering

---

##  Reactive UI

The app uses `StateFlow` to manage UI state.

This allows:

* Automatic UI updates
* Clean separation between state and rendering
* Easy combination of filters (search + favorites)

---

##  UX Decisions

* Real-time search (updates on each keystroke)
* Case-insensitive and accent-insensitive search
* Favorites toggle
* Landscape mode:

  * Split view (list + map)
* Portrait mode:

  * Navigation between screens

---

##  Testing

Unit tests were implemented for:

### Search logic

* Prefix matching
* Case insensitivity
* Empty query behavior
* No-results scenarios
* Accent handling

### Normalization

* Lowercasing
* Removing accents
* Edge cases (empty strings)

These tests ensure that the core search behavior is reliable and correct.

---

##  Conclusion

This implementation prioritizes:

* Efficient algorithms for large datasets
* Clean and maintainable architecture
* Responsive and user-friendly UI

The combination of **pre-sorted data + binary search** is the key factor enabling fast performance at scale.

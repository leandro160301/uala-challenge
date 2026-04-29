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

## Data Handling Strategy

For the purposes of this challenge, **search latency was prioritised over initial loading time and memory usage**.

The full dataset (~200k cities) is loaded into memory and kept cached to enable:

- Instant prefix-based searches
- Predictable performance (`O(log n + k)`)
- No UI blocking during filtering

### Trade-offs

- Higher memory consumption
- No incremental loading

### Production Consideration

In a real-world scenario, this approach could evolve into a **Room + Paging 3** solution to support:

- Incremental data loading
- Persistence across sessions
- Better memory efficiency for very large datasets

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

- The networking requirements are minimal (single GET request)
- Using `HttpURLConnection` avoids unnecessary abstraction
- Keeps the implementation lightweight and easy to follow

### Trade-offs

- More boilerplate compared to Retrofit
- Manual handling of response parsing and errors

### Production Consideration

In a real-world scenario, this layer would likely be implemented using **Retrofit + OkHttp** to support:

- Cleaner API definitions
- Better error handling
- Interceptors (logging, authentication, etc.)
- Easier scalability for multiple endpoints


---


## Dependency Injection

The project uses **Hilt** for dependency injection.

Reasons for using Hilt:

- Reduces boilerplate compared to manual DI
- Lifecycle-aware components (ViewModel integration)
- Scales better as the project grows
- Improves testability and modularity

Hilt is used to provide:

- Repository implementations
- Use cases
- Data sources

---

##  Persistence (Favorites)

Favorites are stored using **DataStore (Preferences)**.

Reason for choosing DataStore over SharedPreferences:

* Asynchronous and coroutine-friendly
* Safer (no blocking calls)
* Better suited for reactive flows

Favorites are exposed as a `Flow`, allowing the UI to update automatically when they change.

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

## Testing

The project includes both **unit tests** and **UI tests**.

### Unit Tests

Focused on core business logic:

#### Search logic

- Prefix matching
- Case insensitivity
- Empty query behavior
- No-results scenarios
- Accent handling

#### Normalization

- Lowercasing
- Removing accents
- Edge cases (empty strings)

These tests ensure that the core search behavior is reliable and correct.

---

### UI Tests (Compose)

UI behavior is verified using **Jetpack Compose testing APIs** (`androidTest`).

Covered scenarios include:

- Different UI states (loading, empty, success)
- Search input interactions
- Favorites filter interactions
- Detail screen rendering
- Navigation callbacks (e.g. back action)

These tests help ensure that the UI behaves correctly and responds to user interactions as expected.

---

## Navigation

Navigation is implemented using **Navigation Compose**.

This allows:

- Declarative navigation
- Better integration with Compose state
- Cleaner handling of screen transitions

The app supports:

- Portrait mode: navigation between list and detail/map screens
- Landscape mode: split view (list + map simultaneously)

---

##  Conclusion

This implementation prioritizes:

* Efficient algorithms for large datasets
* Clean and maintainable architecture
* Responsive and user-friendly UI

The combination of **pre-sorted data + binary search** is the key factor enabling fast performance at scale.


# 🗺️ Maps Service – Google Maps Integration Microservice

A Spring Boot microservice that provides coordinates from a ZIP code and lists nearby banks using the Google Maps API. This service is consumed by the `bank-service`.

---

## 📁 Project Structure

```
com.nearbybank.maps
├── config               # Configuration classes
├── controller           # REST API endpoints
├── integration          # Google Maps API integration
├── model                # POJOs for Google Maps response and Bank info
├── service              # Business logic to process coordinates and banks
├── MapsServiceApplication.java  # Main class
└── resources
    └── application.properties    # Configurations
```

---

## 📡 API Endpoints

### `GET /api/maps/coordinates?zipcode=10001`

Returns `[latitude, longitude]` for a given ZIP code by calling Google Geocoding API.

#### 🔁 Flow:
- `MapsController` → `MapsService` → `GoogleMapsClient`
- Uses `https://maps.googleapis.com/maps/api/geocode/json`
- Returns latitude & longitude as a String array.

---

### `GET /api/maps/banks?lat=40.7128&lng=-74.0060`

Returns a list of nearby banks around the given latitude and longitude (within 10 miles).

#### 🔁 Flow:
- `MapsController` → `MapsService` → `GoogleMapsClient`
- Uses `https://maps.googleapis.com/maps/api/place/nearbysearch/json`
- Filters and returns a list of `Bank` objects.

#### 📦 Sample Response:

```json
[
  {
    "name": "Chase Bank",
    "address": "123 Main St, New York, NY",
    "latitude": "40.7128",
    "longitude": "-74.0060"
  },
  {
    "name": "Bank of America",
    "address": "456 6th Ave, New York, NY",
    "latitude": "40.7132",
    "longitude": "-74.0055"
  }
]
```

---

## ⚙️ How It Works

- This microservice talks directly to Google Maps APIs using `RestTemplate`.
- Reads the Google API key from `.env` file using `Dotenv`.
- Handles both:
  - **ZIP to Coordinates** (via Geocoding API)
  - **Coordinates to Banks** (via Places API)
- Clean separation of concerns using:
  - `controller` for endpoints,
  - `service` for logic,
  - `integration` for external API calls,
  - `model` for mapping API responses

---

## ▶️ Running the Project in Eclipse

1. Import the project as a **Gradle Project** into Eclipse.
2. Ensure `.env` file contains your `GOOGLE_MAPS_API_KEY`.
3. Refresh Gradle and resolve dependencies.
4. Open `MapsServiceApplication.java`.
5. Right-click → **Run As > Java Application**.
6. Runs on `http://localhost:8081` (or configured port in `application.properties`).

Ensure this service is running before using the `bank-service`.

---

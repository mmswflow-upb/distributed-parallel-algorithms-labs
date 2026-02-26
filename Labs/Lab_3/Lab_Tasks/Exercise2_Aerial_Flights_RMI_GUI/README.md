# Exercise 2 — Aerial Flights System with GUI using Java RMI

## What you implemented

A minimal multi-airline reservation system using RMI:

- Each **airline company** runs its own RMI server (`AirlineServer`) and exposes an `AirlineService`.
- The client is a **Swing GUI** (`FlightsClientGUI`) that can:
  - connect to different airlines
  - list their flights
  - reserve a ticket (returns a `Seat` object)
  - build an **itinerary containing seats from multiple airlines** (book a multi-airline travel)

This meets the requirement: the client can reserve tickets from multiple airplane companies and a travel can involve multiple airlines.

---

## Compile

From this folder:

```bash
javac *.java
```

## Run (3+ terminals)

### Terminal 1 — start registry (from this folder)

```bash
rmiregistry 1099
```

### Terminal 2 — start AirlineA server

```bash
java AirlineServer AirlineA AirlineA
```

### Terminal 3 — start AirlineB server

```bash
java AirlineServer AirlineB AirlineB
```

### Terminal 4 — start the GUI client

```bash
java FlightsClientGUI
```

---

## How to test multi-airline travel

1. In the GUI, select **AirlineA**, reserve a flight (adds a Seat to your itinerary).
2. Switch to **AirlineB**, reserve another flight (adds a Seat from a different airline).
3. Your itinerary list will now contain multiple seats from multiple airlines.

---

## Notes

- Data is in-memory for simplicity (resets on server restart).
- Booking is synchronized in the server implementation to avoid race conditions on seat counts.
- The GUI assumes the servers are running on `localhost` and registry port `1099`.

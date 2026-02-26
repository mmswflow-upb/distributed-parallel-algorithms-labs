# Exercise 1 — Enriched Java RMI Calculator (add / sub / mul / div)

## What you implemented

You extended the Lab 3 calculator to support:

- Addition
- Subtraction
- Multiplication
- Division (with division-by-zero check)

## Compile

From this folder:

```bash
javac *.java
```

## Run (3 terminals)

**Terminal 1 (start registry, from this folder):**

```bash
rmiregistry 1099
```

**Terminal 2 (server):**

```bash
java CalculatorServer
```

**Terminal 3 (client):**

```bash
java CalculatorClient
```

You should see outputs for add/sub/mul/div and a handled division-by-zero error.

# Lab 3 — Java RMI Calculator

This folder contains the 4 Java files from the lab handout:

- `Calculator.java`
- `CalculatorImpl.java`
- `CalculatorServer.java`
- `CalculatorClient.java`

## Compile

```bash
javac Calculator.java
javac CalculatorImpl.java
javac CalculatorServer.java
javac CalculatorClient.java
```

> Note: Older RMI tutorials mention `rmic` to generate stub/skeleton classes. With modern Java (5+), dynamic stubs make `rmic` unnecessary for this simple setup.

## Run (3 terminals)

1. Start the RMI registry **from this folder**:

```bash
rmiregistry 1099
```

1. Start the server:

```bash
java CalculatorServer
```

1. Start the client:

```bash
java CalculatorClient
```

Expected output:

```text
1
```

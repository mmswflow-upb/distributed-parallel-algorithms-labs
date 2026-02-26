# Exercise 2 — GCD/LCM RPC (UDP)

This folder contains a UDP-based RPC implementation that computes:

- `gcd n1 n2 ... nk`  → greatest common divisor of a list
- `lcm n1 n2 ... nk`  → least common multiple of a list

## Ports

- Server listens on **1200**
- Client receives replies on **1300**

## Compile

```bash
javac Client.java RPCServer.java
```

## Run

Terminal 1:

```bash
java RPCServer
```

Terminal 2:

```bash
java Client
```

Examples:

```
gcd 12 18 24
Result = 6

lcm 3 4 5
Result = 60
```

To quit: type `q`.

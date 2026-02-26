# Exercise 1 — Calculator RPC (UDP)

This folder extends the lab's UDP-based RPC example so it supports:

- `add a b`
- `sub a b`
- `mul a b`
- `div a b` (returns a decimal if needed)

## Ports

- Server listens on **1200**
- Client receives replies on **1300**

Make sure no other program is using these ports.

## Compile

From this folder:

```bash
javac Client.java RPCServer.java
```

## Run

In terminal 1:

```bash
java RPCServer
```

In terminal 2:

```bash
java Client
```

Example:

```
add 3 4
Result = 7
```

To quit: type `q`.

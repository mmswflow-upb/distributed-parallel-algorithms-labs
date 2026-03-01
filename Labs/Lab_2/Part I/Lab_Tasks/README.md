# Lab_Tasks (Same tools as the demo)

This implementation uses **the exact same tools/pattern as the PDF demo**:

- `Thread` for concurrency
- `BlockingQueue<Request>` for client -> server requests
- `BlockingQueue<Response>` per client for server -> client responses
- Shared-memory "simulation" (no sockets / no networking stack)

## Project

`Lab_Tasks/image_storage_queue/imagestoreq/`

Files:
- `Request.java`, `Response.java` (same idea as the demo)
- `Server.java` (shared store keyword -> images)
- `Terminal.java` (interactive client)
- `Main.java` (starts server + terminal)

## Compile & Run

From `Lab_Tasks/image_storage_queue`:

```bash
javac imagestoreq/*.java
java imagestoreq.Main
```

## Terminal commands

- `post <keyword> <filePath>`
- `list <keyword>`
- `get <keyword> [filenameHint]`
- `help`
- `exit`

Downloaded images are saved to `Lab_Tasks/image_storage_queue/downloads/`.

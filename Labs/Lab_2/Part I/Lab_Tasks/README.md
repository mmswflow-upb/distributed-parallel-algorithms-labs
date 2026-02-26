# Lab_Tasks (Same tools as the demo)

This implementation uses **the exact same tools/pattern as the PDF demo**:

- `Thread` for concurrency
- `BlockingQueue<Request>` for client → server requests
- `BlockingQueue<Response>` per client for server → client responses
- Shared-memory "simulation" (✅ no sockets / no networking stack)

## Project

`Lab_Tasks/image_storage_queue/imagestoreq/`

Files:
- `Request.java`, `Response.java` (same idea as the demo)
- `Server.java` (shared store keyword → images)
- `Terminal.java` (client)
- `Main.java` (starts server + multiple terminals)

## Compile & Run

From `Lab_Tasks/image_storage_queue`:

```bash
javac imagestoreq/*.java
java imagestoreq.Main
```

## Posting real images (optional)

By default, the terminals in `Main.java` have `null` file paths, so they only LIST/GET.

To test POST with real files:
1) Edit `Main.java`
2) Replace `null` with a real path, e.g. `"/home/you/Pictures/cat.jpg"`

The downloaded images will be saved to `Lab_Tasks/image_storage_queue/downloads/`.

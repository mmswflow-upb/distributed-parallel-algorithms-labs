package imagestoreq;

import java.util.*;
import java.util.concurrent.BlockingQueue;

/**
 * Same pattern as the PDF demo:
 *  - Clients (terminals) send Request objects to a shared BlockingQueue<Request>
 *  - Server processes requests and puts Response objects into the specific client's response queue
 *
 * This is a *simulation* of client/server (shared-memory, no networking).
 */
public class Server implements Runnable {

    // keyword -> list of images
    private final Map<String, List<ImageData>> store = new HashMap<>();

    protected final BlockingQueue<Request> requests;
    protected final Map<Integer, BlockingQueue<Response>> responses = new HashMap<>();

    public Server(BlockingQueue<Request> requests) {
        this.requests = requests;
    }

    public void connect(int terminalId, BlockingQueue<Response> responseQueue) {
        this.responses.put(terminalId, responseQueue);
    }

    @Override
    public void run() {
        try {
            while (true) {
                Request req = requests.take();
                switch (req.function) {
                    case "post":
                        // args: Object[]{ keyword(String), image(ImageData) }
                        Object[] postArgs = (Object[]) req.args;
                        String kw = (String) postArgs[0];
                        ImageData img = (ImageData) postArgs[1];

                        boolean ok = post(kw, img);
                        responses.get(req.terminalId).put(new Response("post", ok));
                        break;

                    case "list":
                        // args: keyword(String)
                        String keyword = (String) req.args;
                        List<String> filenames = list(keyword);
                        responses.get(req.terminalId).put(new Response("list", filenames));
                        break;

                    case "get":
                        // args: Object[]{ keyword(String), filenameOrNull(String) }
                        Object[] getArgs = (Object[]) req.args;
                        String k = (String) getArgs[0];
                        String name = (String) getArgs[1];

                        ImageData data = get(k, name);
                        responses.get(req.terminalId).put(new Response("get", data));
                        break;

                    default:
                        responses.get(req.terminalId).put(new Response("error", "Unknown function: " + req.function));
                        break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // synchronized because it mutates shared state (same idea as book() in demo)
    public synchronized boolean post(String keyword, ImageData img) {
        if (keyword == null || keyword.isBlank() || img == null || img.bytes == null) return false;
        store.computeIfAbsent(keyword, kk -> new ArrayList<>()).add(img);
        return true;
    }

    // read-only: can be non-synchronized (same idea as getReservationStatus() in demo)
    public List<String> list(String keyword) {
        List<ImageData> imgs = store.getOrDefault(keyword, Collections.emptyList());
        List<String> out = new ArrayList<>();
        for (ImageData i : imgs) out.add(i.filename);
        return out;
    }

    public ImageData get(String keyword, String filenameOrNull) {
        List<ImageData> imgs = store.getOrDefault(keyword, Collections.emptyList());
        if (imgs.isEmpty()) return null;

        if (filenameOrNull == null || filenameOrNull.isBlank()) {
            return imgs.get(imgs.size() - 1); // latest
        }

        // find by substring match (from newest to oldest)
        for (int i = imgs.size() - 1; i >= 0; i--) {
            if (imgs.get(i).filename.contains(filenameOrNull)) return imgs.get(i);
        }
        return null;
    }
}

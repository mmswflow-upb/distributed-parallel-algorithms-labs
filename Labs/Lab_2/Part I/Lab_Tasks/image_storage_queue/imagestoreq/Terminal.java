package imagestoreq;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Client terminal (same role as "Terminal" in the PDF demo).
 * Sends a few sample requests, then prints responses.
 */
public class Terminal implements Runnable {

    protected final Server server;
    protected final int terminalId;
    protected final BlockingQueue<Request> requests;
    protected final BlockingQueue<Response> responses;

    // optional: file to post
    protected final String keyword;
    protected final String filePath;

    public Terminal(int terminalId,
                    BlockingQueue<Request> requests,
                    BlockingQueue<Response> responses,
                    Server server,
                    String keyword,
                    String filePath) {
        this.terminalId = terminalId;
        this.requests = requests;
        this.responses = responses;
        this.server = server;
        this.keyword = keyword;
        this.filePath = filePath;
        this.server.connect(this.terminalId, this.responses);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        try {
            // 1) POST (if a file path is provided)
            if (filePath != null) {
                Path p = Paths.get(filePath);
                byte[] bytes = Files.readAllBytes(p);
                ImageData img = new ImageData(p.getFileName().toString(), bytes);
                requests.put(new Request(terminalId, "post", new Object[]{keyword, img}));
            }

            // 2) LIST keyword
            requests.put(new Request(terminalId, "list", keyword));

            // 3) GET latest
            requests.put(new Request(terminalId, "get", new Object[]{keyword, null}));

            // read responses forever (same as demo)
            while (true) {
                Response res = responses.take();
                switch (res.function) {
                    case "post":
                        boolean ok = (boolean) res.data;
                        System.out.println(terminalId + ": POST -> " + (ok ? "OK" : "FAILED"));
                        break;

                    case "list":
                        List<String> names = (List<String>) res.data;
                        System.out.println(terminalId + ": LIST '" + keyword + "' -> " + names);
                        break;

                    case "get":
                        ImageData img = (ImageData) res.data;
                        if (img == null) {
                            System.out.println(terminalId + ": GET '" + keyword + "' -> NOT FOUND");
                        } else {
                            // save into downloads/
                            Files.createDirectories(Paths.get("downloads"));
                            Path out = Paths.get("downloads").resolve(terminalId + "_" + img.filename);
                            Files.write(out, img.bytes);
                            System.out.println(terminalId + ": GET '" + keyword + "' -> saved " + out.toString());
                        }
                        break;

                    case "error":
                        System.out.println(terminalId + ": ERROR -> " + res.data);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

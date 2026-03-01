package imagestoreq;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

/**
 * Interactive client terminal.
 *
 * Commands:
 *  - post <keyword> <filePath>
 *  - list <keyword>
 *  - get <keyword> [filenameHint]
 *  - help
 *  - exit
 */
public class Terminal implements Runnable {

    protected final Server server;
    protected final int terminalId;
    protected final BlockingQueue<Request> requests;
    protected final BlockingQueue<Response> responses;

    public Terminal(int terminalId,
                    BlockingQueue<Request> requests,
                    BlockingQueue<Response> responses,
                    Server server) {
        this.terminalId = terminalId;
        this.requests = requests;
        this.responses = responses;
        this.server = server;
        this.server.connect(this.terminalId, this.responses);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        printHelp();

        while (true) {
            try {
                System.out.print("terminal-" + terminalId + "> ");
                if (!scanner.hasNextLine()) {
                    break;
                }

                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\s+", 4);
                String cmd = parts[0].toLowerCase();

                switch (cmd) {
                    case "post":
                        if (parts.length < 3) {
                            System.out.println("Usage: post <keyword> <filePath>");
                            break;
                        }
                        handlePost(parts[1], line.substring(line.indexOf(parts[2])));
                        break;

                    case "list":
                        if (parts.length < 2) {
                            System.out.println("Usage: list <keyword>");
                            break;
                        }
                        requests.put(new Request(terminalId, "list", parts[1]));
                        handleResponse(parts[1], null);
                        break;

                    case "get":
                        if (parts.length < 2) {
                            System.out.println("Usage: get <keyword> [filenameHint]");
                            break;
                        }
                        String hint = parts.length >= 3 ? parts[2] : null;
                        requests.put(new Request(terminalId, "get", new Object[]{parts[1], hint}));
                        handleResponse(parts[1], hint);
                        break;

                    case "help":
                        printHelp();
                        break;

                    case "exit":
                        System.out.println("Exiting terminal " + terminalId + ".");
                        return;

                    default:
                        System.out.println("Unknown command: " + cmd);
                        printHelp();
                        break;
                }
            } catch (Exception e) {
                System.out.println("Command failed: " + e.getMessage());
            }
        }
    }

    private void handlePost(String keyword, String filePathRaw) throws Exception {
        Path p = Paths.get(filePathRaw).normalize();
        if (!Files.exists(p) || !Files.isRegularFile(p)) {
            System.out.println("File not found: " + p);
            return;
        }

        byte[] bytes = Files.readAllBytes(p);
        ImageData img = new ImageData(p.getFileName().toString(), bytes);

        requests.put(new Request(terminalId, "post", new Object[]{keyword, img}));
        handleResponse(keyword, null);
    }

    private void handleResponse(String keyword, String filenameHint) throws Exception {
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
                    System.out.println(terminalId + ": GET '" + keyword + "' -> NOT FOUND"
                            + (filenameHint == null ? "" : " (hint: " + filenameHint + ")"));
                } else {
                    Files.createDirectories(Paths.get("downloads"));
                    Path out = Paths.get("downloads").resolve(terminalId + "_" + img.filename);
                    Files.write(out, img.bytes);
                    System.out.println(terminalId + ": GET '" + keyword + "' -> saved " + out);
                }
                break;

            case "error":
                System.out.println(terminalId + ": ERROR -> " + res.data);
                break;

            default:
                System.out.println(terminalId + ": Unexpected response type: " + res.function);
                break;
        }
    }

    private void printHelp() {
        System.out.println("Commands:");
        System.out.println("  post <keyword> <filePath>      Upload an image under keyword");
        System.out.println("  list <keyword>                 List uploaded image names for keyword");
        System.out.println("  get <keyword> [filenameHint]   Download latest or matching image");
        System.out.println("  help                           Show commands");
        System.out.println("  exit                           Quit");
    }
}

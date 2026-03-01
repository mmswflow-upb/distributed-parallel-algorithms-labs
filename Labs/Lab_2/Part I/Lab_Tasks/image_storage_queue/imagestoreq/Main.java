package imagestoreq;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Starts one interactive Terminal and one Server thread.
 */
public class Main {
    public static void main(String[] args) {
        BlockingQueue<Request> requests = new ArrayBlockingQueue<Request>(50);
        Server server = new Server(requests);

        Thread serverThread = new Thread(server, "image-store-server");
        serverThread.setDaemon(true);
        serverThread.start();

        Terminal terminal = new Terminal(1, requests, new ArrayBlockingQueue<Response>(20), server);
        terminal.run();
    }
}

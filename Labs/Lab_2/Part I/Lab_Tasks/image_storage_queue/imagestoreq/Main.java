package imagestoreq;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Starts the Server thread and multiple Terminal threads (clients).
 *
 * Usage:
 *  - Put some images in the same folder (e.g., cat.jpg, dog.png)
 *  - Then run: java imagestoreq.Main
 *
 * Edit the hardcoded file paths below to match your machine.
 */
public class Main {
    public static void main(String[] args) {
        BlockingQueue<Request> requests = new ArrayBlockingQueue<Request>(50);
        Server server = new Server(requests);

        // Adjust these paths to files that exist on your machine.
        // If a path is null, that terminal will only LIST/GET.
        Terminal t1 = new Terminal(1, requests, new ArrayBlockingQueue<Response>(20), server,
                "cats", "/home/mmswflow/Documents/uni-labs/Sem II/distributed-parallel-algorithms-labs/Labs/Lab_2/Part I/Lab_Tasks/image_storage_queue/imagestoreq/Pics/cat.webp");

        Terminal t2 = new Terminal(2, requests, new ArrayBlockingQueue<Response>(20), server,
                "cats", "/home/mmswflow/Documents/uni-labs/Sem II/distributed-parallel-algorithms-labs/Labs/Lab_2/Part I/Lab_Tasks/image_storage_queue/imagestoreq/Pics/TwoCats.webp");

        Terminal t3 = new Terminal(3, requests, new ArrayBlockingQueue<Response>(20), server,
                "dogs", "/home/mmswflow/Documents/uni-labs/Sem II/distributed-parallel-algorithms-labs/Labs/Lab_2/Part I/Lab_Tasks/image_storage_queue/imagestoreq/Pics/dog.webp");

        new Thread(server).start();
        new Thread(t1).start();
        new Thread(t2).start();
        new Thread(t3).start();
    }
}

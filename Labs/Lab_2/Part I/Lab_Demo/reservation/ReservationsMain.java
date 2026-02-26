package reservation;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ReservationsMain {
    public static void main(String[] args) {
        BlockingQueue<Request> requests = new ArrayBlockingQueue<Request>(10);
        Server server = new Server(requests);

        Terminal t1 = new Terminal(1, requests, new ArrayBlockingQueue<Response>(10), server);
        Terminal t2 = new Terminal(2, requests, new ArrayBlockingQueue<Response>(10), server);
        Terminal t3 = new Terminal(3, requests, new ArrayBlockingQueue<Response>(10), server);

        new Thread(server).start();
        new Thread(t1).start();
        new Thread(t2).start();
        new Thread(t3).start();
    }
}

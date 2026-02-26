import java.io.*;
import java.net.*;

/**
 * UDP-based RPC client for GCD/LCM of multiple numbers.
 *
 * Send:
 *   gcd 12 18 24
 *   lcm 3 4 5
 */
class Client {
    private static final int SERVER_PORT = 1200;
    private static final int CLIENT_PORT = 1300;

    Client() {
        try {
            InetAddress serverAddr = InetAddress.getLocalHost();
            DatagramSocket sendSocket = new DatagramSocket();
            DatagramSocket recvSocket = new DatagramSocket(CLIENT_PORT);

            System.out.println("\nRPC Client (Exercise 2)\n");
            System.out.println("Type one of:");
            System.out.println("  gcd <n1> <n2> ... <nk>");
            System.out.println("  lcm <n1> <n2> ... <nk>");
            System.out.println("Type q to quit.\n");

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String line = br.readLine();
                if (line == null) break;

                if (line.trim().equalsIgnoreCase("q")) {
                    byte[] b = "q".getBytes();
                    DatagramPacket dp = new DatagramPacket(b, b.length, serverAddr, SERVER_PORT);
                    sendSocket.send(dp);
                    System.out.println("Bye.");
                    break;
                }

                byte[] b = line.getBytes();
                DatagramPacket dp = new DatagramPacket(b, b.length, serverAddr, SERVER_PORT);
                sendSocket.send(dp);

                byte[] buf = new byte[4096];
                DatagramPacket resp = new DatagramPacket(buf, buf.length);
                recvSocket.receive(resp);

                String result = new String(resp.getData(), 0, resp.getLength());
                System.out.println("Result = " + result + "\n");
            }

            recvSocket.close();
            sendSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}

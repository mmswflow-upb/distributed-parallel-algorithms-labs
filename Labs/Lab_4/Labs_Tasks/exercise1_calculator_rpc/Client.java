import java.io.*;
import java.net.*;

/**
 * UDP-based RPC client.
 * Sends a line like:  add 3 4
 * Server replies with a single-line result.
 */
class Client {
    private static final int SERVER_PORT = 1200;
    private static final int CLIENT_PORT = 1300;

    Client() {
        try {
            InetAddress serverAddr = InetAddress.getLocalHost();

            // Socket used to SEND requests (ephemeral port is fine)
            DatagramSocket sendSocket = new DatagramSocket();

            // Socket used to RECEIVE responses (must match the port server sends to)
            DatagramSocket recvSocket = new DatagramSocket(CLIENT_PORT);

            System.out.println("\nRPC Client (Exercise 1)\n");
            System.out.println("Type one of:");
            System.out.println("  add <a> <b>");
            System.out.println("  sub <a> <b>");
            System.out.println("  mul <a> <b>");
            System.out.println("  div <a> <b>");
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

                // Wait for response
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

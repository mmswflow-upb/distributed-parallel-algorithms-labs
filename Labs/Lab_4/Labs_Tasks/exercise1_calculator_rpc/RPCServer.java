import java.net.*;
import java.util.*;

/**
 * UDP-based RPC server (Exercise 1).
 * Listens on port 1200. Sends responses to localhost:1300.
 *
 * Supported methods: add, sub, mul, div
 */
class RPCServer {
    private static final int SERVER_PORT = 1200;
    private static final int CLIENT_PORT = 1300;

    RPCServer() {
        try (DatagramSocket serverSocket = new DatagramSocket(SERVER_PORT)) {
            System.out.println("RPC Server (Exercise 1) listening on port " + SERVER_PORT);

            byte[] buf = new byte[4096];

            while (true) {
                DatagramPacket req = new DatagramPacket(buf, buf.length);
                serverSocket.receive(req);

                String msg = new String(req.getData(), 0, req.getLength()).trim();
                if (msg.equalsIgnoreCase("q")) {
                    System.out.println("Received quit. Exiting.");
                    break;
                }

                System.out.println("Request: " + msg);

                String result;
                try {
                    result = handle(msg);
                } catch (Exception ex) {
                    result = "ERROR: " + ex.getMessage();
                }

                byte[] out = result.getBytes();
                DatagramPacket resp = new DatagramPacket(
                        out,
                        out.length,
                        InetAddress.getLocalHost(),
                        CLIENT_PORT
                );
                serverSocket.send(resp);
                System.out.println("Response: " + result + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String handle(String msg) {
        StringTokenizer st = new StringTokenizer(msg, " ");
        if (st.countTokens() != 3) {
            throw new IllegalArgumentException("Expected: <method> <a> <b>");
        }

        String method = st.nextToken();
        int a = Integer.parseInt(st.nextToken());
        int b = Integer.parseInt(st.nextToken());

        switch (method.toLowerCase()) {
            case "add":
                return String.valueOf(add(a, b));
            case "sub":
                return String.valueOf(sub(a, b));
            case "mul":
                return String.valueOf(mul(a, b));
            case "div":
                if (b == 0) return "ERROR: division by zero";
                // Use double so division behaves as expected for non-multiples
                double d = div(a, b);
                // Clean formatting: show integer-looking doubles without .0
                if (Math.floor(d) == d) return String.valueOf((long) d);
                return String.valueOf(d);
            default:
                return "ERROR: unknown method '" + method + "'";
        }
    }

    public int add(int a, int b) { return a + b; }
    public int sub(int a, int b) { return a - b; }
    public int mul(int a, int b) { return a * b; }
    public double div(int a, int b) { return (double) a / (double) b; }

    public static void main(String[] args) {
        new RPCServer();
    }
}

import java.net.*;
import java.util.*;

/**
 * UDP-based RPC server for:
 *   - gcd of a list of integers
 *   - lcm of a list of integers
 *
 * Message format:
 *   gcd n1 n2 ... nk
 *   lcm n1 n2 ... nk
 */
class RPCServer {
    private static final int SERVER_PORT = 1200;
    private static final int CLIENT_PORT = 1300;

    RPCServer() {
        try (DatagramSocket serverSocket = new DatagramSocket(SERVER_PORT)) {
            System.out.println("RPC Server (Exercise 2) listening on port " + SERVER_PORT);

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
                DatagramPacket resp = new DatagramPacket(out, out.length, InetAddress.getLocalHost(), CLIENT_PORT);
                serverSocket.send(resp);
                System.out.println("Response: " + result + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String handle(String msg) {
        StringTokenizer st = new StringTokenizer(msg, " ");
        if (!st.hasMoreTokens()) throw new IllegalArgumentException("Empty request");

        String method = st.nextToken().toLowerCase();

        List<Long> nums = new ArrayList<>();
        while (st.hasMoreTokens()) {
            nums.add(Long.parseLong(st.nextToken()));
        }
        if (nums.size() < 2) {
            throw new IllegalArgumentException("Provide at least 2 numbers");
        }

        switch (method) {
            case "gcd":
                return String.valueOf(gcdList(nums));
            case "lcm":
                return String.valueOf(lcmList(nums));
            default:
                return "ERROR: unknown method '" + method + "'";
        }
    }

    private long gcd(long a, long b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            long t = a % b;
            a = b;
            b = t;
        }
        return a;
    }

    private long gcdList(List<Long> nums) {
        long g = nums.get(0);
        for (int i = 1; i < nums.size(); i++) {
            g = gcd(g, nums.get(i));
        }
        return g;
    }

    private long lcm(long a, long b) {
        a = Math.abs(a);
        b = Math.abs(b);
        if (a == 0 || b == 0) return 0;
        long g = gcd(a, b);
        // Reduce overflow risk a bit by dividing first
        return (a / g) * b;
    }

    private long lcmList(List<Long> nums) {
        long l = nums.get(0);
        for (int i = 1; i < nums.size(); i++) {
            l = lcm(l, nums.get(i));
        }
        return l;
    }

    public static void main(String[] args) {
        new RPCServer();
    }
}

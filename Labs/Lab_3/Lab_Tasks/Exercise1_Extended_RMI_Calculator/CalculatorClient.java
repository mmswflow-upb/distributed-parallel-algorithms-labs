import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;

/**
 * Client that looks up the remote CalculatorService and demonstrates all operations.
 */
public class CalculatorClient {
    public static void main(String[] args) {
        try {
            Calculator c = (Calculator) Naming.lookup("rmi://localhost/CalculatorService");

            long a = 12, b = 3;
            System.out.println("add(" + a + "," + b + ") = " + c.add(a, b));
            System.out.println("sub(" + a + "," + b + ") = " + c.sub(a, b));
            System.out.println("mul(" + a + "," + b + ") = " + c.mul(a, b));
            System.out.println("div(" + a + "," + b + ") = " + c.div(a, b));

            // Division by zero demo
            try {
                System.out.println("div(10,0) = " + c.div(10, 0));
            } catch (ArithmeticException ae) {
                System.out.println("Expected error: " + ae.getMessage());
            }
        }
        catch (MalformedURLException murle) {
            System.out.println("MalformedURLException");
            System.out.println(murle);
        }
        catch (RemoteException re) {
            System.out.println("RemoteException");
            System.out.println(re);
        }
        catch (NotBoundException nbe) {
            System.out.println("NotBoundException");
            System.out.println(nbe);
        }
    }
}

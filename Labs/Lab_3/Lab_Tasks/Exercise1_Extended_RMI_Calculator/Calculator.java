import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Exercise 1: Enriched RMI Calculator
 * Now supports add, sub, mul, div.
 */
public interface Calculator extends Remote {
    long add(long a, long b) throws RemoteException;
    long sub(long a, long b) throws RemoteException;
    long mul(long a, long b) throws RemoteException;
    double div(double a, double b) throws RemoteException, ArithmeticException;
}

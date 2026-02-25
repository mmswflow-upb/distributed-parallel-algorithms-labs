package Lab_Demo;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface for the RMI Calculator service.
 */
public interface Calculator extends Remote {
    long sub(long a, long b) throws RemoteException;
}

package Lab_Demo;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Remote object implementation.
 */
public class CalculatorImpl extends UnicastRemoteObject implements Calculator {

    // Implementations must have an explicit constructor to declare RemoteException.
    public CalculatorImpl() throws RemoteException {
        super();
    }

    @Override
    public long sub(long a, long b) throws RemoteException {
        return a - b;
    }
}

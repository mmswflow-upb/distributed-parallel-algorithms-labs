import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Remote object implementation.
 */
public class CalculatorImpl extends UnicastRemoteObject implements Calculator {

    public CalculatorImpl() throws RemoteException {
        super();
    }

    @Override
    public long add(long a, long b) throws RemoteException {
        return a + b;
    }

    @Override
    public long sub(long a, long b) throws RemoteException {
        return a - b;
    }

    @Override
    public long mul(long a, long b) throws RemoteException {
        return a * b;
    }

    @Override
    public double div(double a, double b) throws RemoteException, ArithmeticException {
        if (b == 0.0) throw new ArithmeticException("Division by zero");
        return a / b;
    }
}

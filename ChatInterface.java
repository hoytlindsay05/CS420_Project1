import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatInterface extends Remote {
    void registerClient(String name, ChatClientInterface client) throws RemoteException;
    ChatClientInterface getClient(String name) throws RemoteException;
}

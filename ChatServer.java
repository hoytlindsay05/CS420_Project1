import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

public class ChatServer extends UnicastRemoteObject implements ChatInterface {
    private Map<String, ChatClientInterface> clientRegistry;

    protected ChatServer() throws RemoteException {
        clientRegistry = new HashMap<>();
    }

    @Override
    public synchronized void registerClient(String name, ChatClientInterface client) throws RemoteException {
        clientRegistry.put(name, client);
        System.out.println("Client registered: " + name);
    }

    @Override
    public synchronized ChatClientInterface getClient(String name) throws RemoteException {
        return clientRegistry.get(name);
    }

    public static void main(String[] args) {
        try {
            // Start RMI registry programmatically
            Registry registry = LocateRegistry.createRegistry(1099);
            System.out.println("RMI registry started on port 1099.");

            // Bind the server
            ChatServer server = new ChatServer();
            registry.rebind("ChatService", server);
            System.out.println("Chat server started and bound to 'ChatService'.");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ChatClient extends UnicastRemoteObject implements ChatClientInterface {
    private String name;
    private ChatInterface chatServer;

    protected ChatClient(String name, ChatInterface chatServer) throws RemoteException {
        this.name = name;
        this.chatServer = chatServer;

        // Register with the server
        chatServer.registerClient(name, this);
        System.out.println("Registered as: " + name);
    }

    @Override
    public void receiveMessage(String message) throws RemoteException {
        System.out.println(message);
    }

    public void sendMessage(String recipientName, String message) throws RemoteException {
        ChatClientInterface recipient = chatServer.getClient(recipientName);
        if (recipient != null) {
            recipient.receiveMessage(name + ": " + message);
            System.out.println("Message sent to " + recipientName);
        } else {
            System.out.println("Recipient not found: " + recipientName);
        }
    }

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ChatInterface chatServer = (ChatInterface) registry.lookup("ChatService");

            // Ask user for name
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();

            ChatClient client = new ChatClient(name, chatServer);

            System.out.println("Chat system ready. Use the format: [recipient_name] [message]");

            while (true) {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("exit")) break;

                String[] parts = input.split(" ", 2);
                if (parts.length == 2) {
                    String recipientName = parts[0];
                    String message = parts[1];
                    client.sendMessage(recipientName, message);
                } else {
                    System.out.println("Invalid format. Use: [recipient_name] [message]");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

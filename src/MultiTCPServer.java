import java.net.*;
import java.io.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MultiTCPServer {
    private static List<ClientHandler> clientHandlers = new CopyOnWriteArrayList<>();
    public static void main(String args[]) {
        try {
            int serverPort = 6666; // the server port
            ServerSocket serverSocket = new ServerSocket(serverPort);
            while (serverSocket.isBound()) {
                System.out.println("Aguardando conexao no endereco: " + InetAddress.getLocalHost() + ":" + serverPort);
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket, clientHandlers);
                clientHandlers.add(handler);//unica mudança feita foi essa parte para ter vários handlers.
                handler.start();
                System.out.println("Conexao feita com: "
                        + clientSocket.getInetAddress() + ":"
                        + clientSocket.getPort());
            }
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Listen socket:" + e.getMessage());
        }
    }
}
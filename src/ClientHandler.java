import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
//maior mudança
class ClientHandler extends Thread {
    private DataInputStream in;
    private DataOutputStream out;
    private Socket clientSocket;
    private List<ClientHandler> clientHandlers;
    private String clientUsername;//detalhe que achei que séria legal.

    public ClientHandler(Socket aClientSocket, List<ClientHandler> clientHandlers) {//modifiquei o método para recebe uma lista com handlers, só isso.
        try {
            clientSocket = aClientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            this.clientHandlers = clientHandlers;
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    public void run() {
        try {
            out.writeUTF("Please write your user name: ");//pedindo ao user o seu user name
            this.clientUsername = in.readUTF();
            while (true) {
                try {
                    String data = in.readUTF();
                    System.out.println("Mensagem Recebida:");
                    System.out.println(data);
                    if (data.equals("/exit")) {
                        System.out.println(clientUsername + "Saiu");
                        break;
                    }
                    broadcastMessage(data, this);
                } catch (EOFException e) {
                    System.out.println("EOF:" + e.getMessage());
                    break;
                } catch (IOException e) {
                    System.out.println("readline:" + e.getMessage());
                    break;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {//quando o usuario sai da conversa
            broadcastMessage("/SAIU DA CONVERSA", this);
            clientHandlers.remove(this);
            System.out.println("Usuario" + clientUsername + "saiu.");
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Something bad happenet to the clientSocket of" + clientUsername);;
            }

        }
    }

    private void broadcastMessage(String data, ClientHandler sender) {//método legal, mas foi dificil entender de primeira.
        // basicamente, isso aqui manda a mensagem para todos os que estão no servidor, pega cada uma das pessoas e manda a mensagem.
        //o usuario sendo o único diferente.
        for (ClientHandler c: clientHandlers) {
            if (!c.equals(sender)) {
                try {
                    c.out.writeUTF(clientUsername + ": " + data);
                } catch (IOException e) {
                    System.out.println("Unable to reach " + c.getClientUsername());;
                }
            }
        }
    }


    public String getClientUsername() {
        return clientUsername;//necessário para ter o nome o user
    }
}
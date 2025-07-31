import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class SimpleTCPClient {

    public void start(String serverIp, int serverPort) throws IOException {
        try (Socket socket = new Socket(serverIp, serverPort);//com isso close() se torna desnecessário, (try-with-resources
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {
            //escuta e escrita do para o serve do ponto do cliente.
            listenerThread(input);
            String message;
            while (true) {
                message = scanner.nextLine();//thread principal ela é tipo a broadcast que manda a mensagem para o servidor.
                if (message.equalsIgnoreCase("/exit")){
                    break;
                }
                output.writeUTF(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void listenerThread(DataInputStream data) {
        Thread thread = new Thread(() -> {
            String mensagemDoServidor;
            try {
                while (true) {
                    mensagemDoServidor = data.readUTF();
                    System.out.println(mensagemDoServidor);
                }
            } catch (IOException e) {
                System.out.println("\n--- Conexão com o servidor foi encerrada ---");
            }
        }); //eu fiz uma thread de "escuta" que recebe a mensagem repassada pelo servidor.
        thread.start();
    }


    public static void main(String[] args) {
        String serverIp = "0.0.0.0";
        int serverPort = 6666;
        try {
            // Cria e roda cliente
            SimpleTCPClient client = new SimpleTCPClient();
            client.start(serverIp, serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package biblioteca;

import java.io.*;
import java.net.Socket;

public class Cliente {
    private static final String HOST = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Conectado ao servidor da biblioteca.");
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.writeUTF(userInput);
                String response;
                while (!(response = in.readUTF()).equals("FIM_LISTA")) {
                    System.out.println(response);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

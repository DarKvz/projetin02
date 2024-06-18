package biblioteca;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Servidor {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor iniciado na porta " + PORT + " e aguardando conexões...");
            Biblioteca biblioteca = new Biblioteca();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());
                new Thread(new ClientHandler(clientSocket, biblioteca)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Biblioteca biblioteca;

    public ClientHandler(Socket socket, Biblioteca biblioteca) {
        this.clientSocket = socket;
        this.biblioteca = biblioteca;
    }

    @Override
    public void run() {
        try (InputStream inputStream = clientSocket.getInputStream();
             OutputStream outputStream = clientSocket.getOutputStream();
             DataInputStream in = new DataInputStream(inputStream);
             DataOutputStream out = new DataOutputStream(outputStream)) {

            while (true) {
                String request = in.readUTF();
                String[] parts = request.split(" ");
                String command = parts[0];
                switch (command) {
                    case "LISTAR":
                        List<Livro> livros = biblioteca.listarLivros();
                        for (Livro livro : livros) {
                            out.writeUTF(livro.toString());
                        }
                        out.writeUTF("FIM_LISTA");
                        break;
                    case "CADASTRAR":
                        if (parts.length == 5) {
                            String autor = parts[1];
                            String nome = parts[2];
                            String genero = parts[3];
                            int numeroDeExemplares = Integer.parseInt(parts[4]);
                            Livro novoLivro = new Livro(autor, nome, genero, numeroDeExemplares);
                            if (biblioteca.cadastrarLivro(novoLivro)) {
                                out.writeUTF("Livro cadastrado com sucesso!");
                            } else {
                                out.writeUTF("Erro ao cadastrar o livro.");
                            }
                        } else {
                            out.writeUTF("Comando inválido. Use: CADASTRAR <autor> <nome> <genero> <numeroDeExemplares>");
                        }
                        break;
                    case "ALUGAR":
                        if (parts.length == 2) {
                            String nomeLivroAlugar = parts[1];
                            if (biblioteca.alugarLivro(nomeLivroAlugar)) {
                                out.writeUTF("Livro alugado com sucesso!");
                            } else {
                                out.writeUTF("Erro ao alugar o livro ou livro não disponível.");
                            }
                        } else {
                            out.writeUTF("Comando inválido. Use: ALUGAR <nome>");
                        }
                        break;
                    case "DEVOLVER":
                        if (parts.length == 2) {
                            String nomeLivroDevolver = parts[1];
                            if (biblioteca.devolverLivro(nomeLivroDevolver)) {
                                out.writeUTF("Livro devolvido com sucesso!");
                            } else {
                                out.writeUTF("Erro ao devolver o livro.");
                            }
                        } else {
                            out.writeUTF("Comando inválido. Use: DEVOLVER <nome>");
                        }
                        break;
                    default:
                        out.writeUTF("Comando não reconhecido.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

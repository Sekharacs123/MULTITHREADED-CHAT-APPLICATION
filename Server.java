import java.io.*;
import java.net.*;
import java.util.*;
public class Server {
    static Vector<ClientHandler> clients = new Vector<>();
    static int clientCount=0;
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        System.out.println(" Server started.please Wait for clients...");
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Client connected!");
            ClientHandler client = new ClientHandler(socket, "User" + clientCount++);
            clients.add(client);
            Thread s = new Thread(client);
            s.start();
        }
    }
}
class ClientHandler implements Runnable {
    private String n;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    public ClientHandler(Socket socket, String n) throws IOException {
        this.socket = socket;
        this.n = n;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }
    public void run() {
        String msg;
        try {
            while ((msg = in.readLine()) != null) {
                System.out.println(n+ ": " + msg);
                for (ClientHandler ch : Server.clients) {
                    if (ch != this) {
                        ch.out.println(n+ ": " + msg);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Client disconnected: " + n);
        }
    }
}

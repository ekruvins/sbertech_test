import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class Server {
    final static Logger logger = Logger.getLogger(Server.class);
    public static void main(String[] args) throws IOException {
        try{
            int serverPort = 5000;
            ServerSocket serverSocket = new ServerSocket(serverPort);
            while (true) {
                Socket server = serverSocket.accept();
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(server.getInputStream()));
                String line = fromClient.readLine();
                PrintWriter toClient = new PrintWriter(server.getOutputStream(), true);
                System.out.println(line);
                toClient.println("Thank you for connecting to " + server.getLocalSocketAddress() + " Goodbye!");
            }
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            try {
                if (e instanceof SocketTimeoutException) {
                    throw new SocketTimeoutException();
                } else {
                    e.printStackTrace();
                }
            } catch (SocketTimeoutException ste) {
                System.out.println("Turn off the server by timeout");
            }
        }
    }
}

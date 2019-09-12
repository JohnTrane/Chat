import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread{
    private final int serverPort;

    private ArrayList<ServerWorker> workerList = new ArrayList<>();

    public Server(int serverPort) {
        this.serverPort = serverPort;
    }

    public List<ServerWorker> getWorkerList(){
        return workerList;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(serverPort);                   // create a socket on selected port
            while(true) {
                System.out.println("About to accept client connection");
                Socket clientSocket = serverSocket.accept();                            // waiting for client to connect
                System.out.println("client connected" + clientSocket);
                ServerWorker worker = new ServerWorker(this, clientSocket);      // move client to a separate Thread to handle multiple connections
                workerList.add(worker);

                worker.start();                                                         // who knows...
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

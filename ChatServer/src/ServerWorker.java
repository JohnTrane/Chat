import com.sun.deploy.util.StringUtils;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.List;

public class ServerWorker extends Thread {

    private final Socket clientSocket;
    private final Server server;
    private String login = null;
    private OutputStream outputStream;

    public ServerWorker(Server server, Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            handleClientSocket();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleClientSocket() throws IOException, InterruptedException {
        InputStream inputStream = clientSocket.getInputStream();
        this.outputStream = clientSocket.getOutputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split(" ");

            if (tokens.length > 0) {
                String cmd = tokens[0];
                if ("/q".equalsIgnoreCase(line)) {
                    break;
                } else if ("/login".equalsIgnoreCase(cmd)) {
                    handleLogin(outputStream, tokens);
                } else {
                    String msg = " " + login + ": " + line + "\r \n";

                    List<ServerWorker> workerList = server.getWorkerList();
                    for(ServerWorker worker : workerList){
                        worker.send(msg);
                    }
                }
            }
        }
        clientSocket.close();
    }

    public String getLogin() {
        return login;
    }



    private void handleLogin(OutputStream outputStream, String[] tokens) throws IOException {
        if(tokens.length == 3){
            String login = tokens[1];
            String password = tokens[2];

            if (login.equalsIgnoreCase("guest") && password.equals("Welcome") || login.equalsIgnoreCase("Ivan") && password.equals("Ivan")){
                this.login = login;
                String onlineMsg = "online " + login + "\r \n";

                List<ServerWorker> workerList = server.getWorkerList();
                for(ServerWorker worker : workerList){
                    worker.send(onlineMsg);
                }

            } else {
                String msg = " user not found \r \n";
                send(msg);
            }
        }
    }

    private void send(String msg) throws IOException {
        outputStream.write(msg.getBytes());
    }
}
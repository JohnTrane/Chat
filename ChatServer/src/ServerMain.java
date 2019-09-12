

public class ServerMain {
    public static void main(String[] args) {
        int port = 8082;                                //set the port
        Server server = new Server(port);               // create server instance
        server.start();                                 // run run() in Server.
    }
}

package server;


import server.network.Server;

public class Main {

    public static void main(String[] args) throws Exception {
        new Server(8888).run();
    }
}

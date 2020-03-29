package client;

import client.gui.Window;
import client.network.NettyClient;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        NettyClient nettyClient = new NettyClient();
        Window window = new Window(nettyClient);
        window.start(primaryStage);
    }
}

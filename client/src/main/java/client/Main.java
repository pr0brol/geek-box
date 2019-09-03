package client;


import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        NettyClient nettyClient = new NettyClient();
        Window window = new Window(nettyClient);
        window.start(primaryStage);
        nettyClient.run();
    }
}

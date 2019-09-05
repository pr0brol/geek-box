package client;

import common.*;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static common.CommandMessage.*;

public class Window {
    private int windowHeight = 480;
    private int windowWidth = 640;

    private NettyClient nettyClient;
    private Data data;

    public Window(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    public void start(Stage primaryStage) {

        AnchorPane root = new AnchorPane();

        Scene scene = new Scene(root, windowWidth, windowHeight);

        data = new Data();

        primaryStage.setOnCloseRequest(event -> System.exit(0));

        TableView<String> areaClient = new TableView<>();
        TableColumn<String, String> pathClientColumn = new TableColumn<>("Client_Path");
        pathClientColumn.setMinWidth(313.0);
        pathClientColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        areaClient.getColumns().add(pathClientColumn);
        areaClient.setEditable(false);
        areaClient.setMinWidth(314);
        areaClient.setMinHeight(415);
        root.getChildren().add(areaClient);
        AnchorPane.setLeftAnchor(areaClient, 5.0);
        AnchorPane.setTopAnchor(areaClient, 30.0);


        TableView<String> areaServer = new TableView<>();
        TableColumn<String, String> pathServerColumn = new TableColumn<>("Server_Path");
        pathServerColumn.setMinWidth(313.0);
        pathServerColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        areaServer.getColumns().add(pathServerColumn);
        areaServer.setEditable(false);
        areaServer.setMinWidth(314);
        areaServer.setMinHeight(415);
        root.getChildren().add(areaServer);
        AnchorPane.setRightAnchor(areaServer, 5.0);
        AnchorPane.setTopAnchor(areaServer, 30.0);

        primaryStage.setTitle("geek-box");
        primaryStage.show();
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);



        Button downloadClient = new Button("загрузить файл");
        downloadClient.setMinWidth(100.0);
        downloadClient.setOnAction(event -> {
            String name = areaClient.getSelectionModel().getSelectedItem();
            try {
                Path path = Paths.get(data.getPATH() + name);
                nettyClient.sendMsg(new FileMessage(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
            refreshServer(areaServer);
            refreshServer(areaServer);
        });

        Button deleteClient = new Button("удалить файл");
        deleteClient.setMinWidth(100.0);
        deleteClient.setOnAction(event -> {
            String path = areaClient.getSelectionModel().getSelectedItem();
            File file = new File(data.getPATH() + path);
            file.delete();
            refreshClient(areaClient);
        });

        Button refreshClient = new Button("обновить");
        refreshClient.setMinWidth(100.0);
        refreshClient.setOnAction(event -> refreshClient(areaClient));

        Button downloadServer = new Button("загрузить файл");
        downloadServer.setMinWidth(100.0);
        downloadServer.setOnAction(event -> {
            String path = areaServer.getSelectionModel().getSelectedItem();
            CommandMessage cm = new CommandMessage(COPY, path);
            nettyClient.sendMsg(cm);
            try {
                Object obj = nettyClient.readObject();                                                               //
                if(obj instanceof FileMessage){                                                                      //
                    FileMessage fm = (FileMessage) obj;                                                              //
                    FileOutputStream fos = null;                                                                     //
                    try {                                                                                            // ГОВНОКОД
                        fos = new FileOutputStream(data.getPATH() + fm.getFilename(), true);          //
                        fos.write(fm.getData());                                                                     //
                    } catch (FileNotFoundException e) {                                                              //
                        e.printStackTrace();                                                                         //
                    } finally {                                                                                      //
                        fos.close();                                                                                 //
                    }                                                                                                //
                }
                refreshClient(areaClient);
                refreshClient(areaClient);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Button deleteServer = new Button("удалить файл");
        deleteServer.setMinWidth(100.0);
        deleteServer.setOnAction(event -> {
            String path = areaServer.getSelectionModel().getSelectedItem();
            CommandMessage cm = new CommandMessage(DELETE, path);
            nettyClient.sendMsg(cm);
            refreshServer(areaServer);
            refreshServer(areaServer);
        });

        Button refreshServer = new Button("обновить");
        refreshServer.setMinWidth(100.0);
        refreshServer.setOnAction(event -> {
            refreshServer(areaServer);
            refreshServer(areaServer);
        });

        root.getChildren().add(downloadClient);
        root.getChildren().add(deleteClient);
        root.getChildren().add(refreshClient);
        AnchorPane.setBottomAnchor(downloadClient, 5.0);
        AnchorPane.setLeftAnchor(downloadClient, 5.0);
        AnchorPane.setLeftAnchor(deleteClient, 112.0);
        AnchorPane.setBottomAnchor(deleteClient, 5.0);
        AnchorPane.setLeftAnchor(refreshClient, 219.0);
        AnchorPane.setBottomAnchor(refreshClient, 5.0);

        root.getChildren().add(downloadServer);
        root.getChildren().add(deleteServer);
        root.getChildren().add(refreshServer);
        AnchorPane.setBottomAnchor(refreshServer, 5.0);
        AnchorPane.setRightAnchor(refreshServer, 5.0);
        AnchorPane.setRightAnchor(deleteServer, 112.0);
        AnchorPane.setBottomAnchor(deleteServer, 5.0);
        AnchorPane.setRightAnchor(downloadServer, 219.0);
        AnchorPane.setBottomAnchor(downloadServer, 5.0);

        Label labelClient = new Label("Local Repository");
        labelClient.setTranslateX(115.0);
        root.getChildren().add(labelClient);
        AnchorPane.setTopAnchor(labelClient, 5.0);
        AnchorPane.setLeftAnchor(labelClient, 5.0);

        Label labelServer = new Label("Cloud Repository");
        labelServer.setTranslateX(-112.0);
        root.getChildren().add(labelServer);
        AnchorPane.setTopAnchor(labelServer, 5.0);
        AnchorPane.setRightAnchor(labelServer, 5.0);

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(() -> {
            try
            {
                nettyClient.run();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });
    }

    public void refreshServer(TableView areaServer){

        CommandMessage cm = new CommandMessage(REFRESH, null);
        nettyClient.sendMsg(cm);
        try {
            Object msg = nettyClient.readObject();
            if(msg instanceof FileRequest){
                FileRequest fr = (FileRequest) msg;
                String[] str = fr.getFilesName();
                areaServer.setItems(null);
                ObservableList<String> table = FXCollections.observableArrayList(str);
                areaServer.setItems(table);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshClient(TableView areaClient){
        String[] str = data.getFilesNames();
        areaClient.setItems(null);
        ObservableList<String> table = FXCollections.observableArrayList(str);
        areaClient.setItems(table);
    }
}
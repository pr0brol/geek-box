package client;

import common.*;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static common.CommandMessage.*;
import static javafx.geometry.Pos.*;

public class Window extends Application {
    private int windowHeight = 480;
    private int windowWidth = 640;

    private NettyClient nettyClient;
    private Data data;

//    public Window(NettyClient nettyClient) {
//        this.nettyClient = nettyClient;
//    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        AnchorPane root = new AnchorPane();

        Scene scene = new Scene(root, windowWidth, windowHeight);

        data = new Data();

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
        });

        Button deleteClient = new Button("удалить файл");
        deleteClient.setMinWidth(100.0);
        deleteClient.setOnAction(event -> {
            String path = areaClient.getSelectionModel().getSelectedItem();
            File file = new File(data.getPATH() + path);
            file.delete();
        });

        Button refreshClient = new Button("обновить");
        refreshClient.setMinWidth(100.0);
        refreshClient.setOnAction(event -> {
            String[] str = data.getFilesNames();
            areaClient.setItems(null);
            ObservableList<String> table = FXCollections.observableArrayList(str);
            areaClient.setItems(table);
        });

        Button downloadServer = new Button("загрузить файл");
        downloadServer.setMinWidth(100.0);
        downloadServer.setOnAction(event -> {
            String path = areaServer.getSelectionModel().getSelectedItem();
            CommandMessage cm = new CommandMessage(COPY, path);
            nettyClient.sendMsg(cm);
        });

        Button deleteServer = new Button("удалить файл");
        deleteServer.setMinWidth(100.0);
        deleteServer.setOnAction(event -> {
            String path = areaServer.getSelectionModel().getSelectedItem();
            CommandMessage cm = new CommandMessage(DELETE, path);
            nettyClient.sendMsg(cm);
        });

        Button refreshServer = new Button("обновить");
        refreshServer.setMinWidth(100.0);
        refreshServer.setOnAction(event -> {
            CommandMessage cm = new CommandMessage(REFRESH, null);
            nettyClient.sendMsg(cm);
            try {

                if(nettyClient.readObject() instanceof FileRequest){
                    FileRequest fr = (FileRequest) nettyClient.readObject();
                    String[] str = fr.getFilesname();
                    areaServer.setItems(null);
                    ObservableList<String> table = FXCollections.observableArrayList(str);
                    areaServer.setItems(table);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
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


    }
}
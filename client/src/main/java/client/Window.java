package client;

import common.*;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static common.CommandMessage.*;

public class Window {
    private int windowHeight = 480;
    private int windowWidth = 640;


    private Data data;
    private NettyClient nettyClient;
    private String userName;

    TableView<String> areaServer;
    TableView<String> areaClient;

    Stage newWindow;

    public Window(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    public TableView<String> getAreaServer() {
        return areaServer;
    }

    public void start(Stage primaryStage) {

        data = new Data();


        AnchorPane root = new AnchorPane();
        StackPane pane = new StackPane();

        Scene scene = new Scene(root, windowWidth, windowHeight);
        Scene firstScene = new Scene(pane, 300, 150);

        primaryStage.setOnCloseRequest(event -> System.exit(0));

        areaClient = new TableView<>();
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

        areaServer = new TableView<>();
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
            if (name != null) {
                try {
                    Path path = Paths.get(data.getPATH() + userName + "/" + name);
                    nettyClient.sendMsg(new FileMessage(path));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                refreshServer();
            }
        });

        Button deleteClient = new Button("удалить файл");
        deleteClient.setMinWidth(100.0);
        deleteClient.setOnAction(event -> {
            String name = areaClient.getSelectionModel().getSelectedItem();
            if (name != null) {
                File file = new File(data.getPATH() + userName + "/" + name);
                file.delete();
                refreshClient(areaClient);
            }
        });

        Button refreshClient = new Button("обновить");
        refreshClient.setMinWidth(100.0);
        refreshClient.setOnAction(event -> refreshClient(areaClient));

        Button downloadServer = new Button("загрузить файл");
        downloadServer.setMinWidth(100.0);
        downloadServer.setOnAction(event -> {
            String name = areaServer.getSelectionModel().getSelectedItem();
            if (name != null) {
                CommandMessage cm = new CommandMessage(COPY, name);
                nettyClient.sendMsg(cm);
                refreshClient(areaClient);
            }
        });

        Button deleteServer = new Button("удалить файл");
        deleteServer.setMinWidth(100.0);
        deleteServer.setOnAction(event -> {
            String name = areaServer.getSelectionModel().getSelectedItem();
            if (name != null) {
                CommandMessage cm = new CommandMessage(DELETE, name);
                nettyClient.sendMsg(cm);
                refreshServer();
            }
        });

        Button refreshServer = new Button("обновить");
        refreshServer.setMinWidth(100.0);
        refreshServer.setOnAction(event -> refreshServer());

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

        newWindow = new Stage();
        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.initOwner(scene.getWindow());
        newWindow.setScene(firstScene);

        Label loginLable = new Label("Логин");
        loginLable.setMinSize(50, 35);
        loginLable.setTranslateX(-120);
        loginLable.setTranslateY(-40);
        pane.getChildren().add(loginLable);

        Label passwordLable = new Label("Пароль");
        passwordLable.setMinSize(50, 35);
        passwordLable.setTranslateX(-120);
        pane.getChildren().add(passwordLable);

        TextField loginField = new TextField();
        loginField.setMaxSize(210, 20);
        loginField.setTranslateY(-40);
        loginField.setTranslateX(35);
        pane.getChildren().add(loginField);

        PasswordField passwordField = new PasswordField();
        passwordField.setMaxSize(210, 20);
        passwordField.setTranslateX(35);
        pane.getChildren().add(passwordField);

        Button cancelBtn = new Button("отмена");
        cancelBtn.setTranslateX(90);
        cancelBtn.setTranslateY(50);
        cancelBtn.setMinSize(100, 35);
        cancelBtn.setOnAction(event -> System.exit(0));
        pane.getChildren().add(cancelBtn);

        Button loginBtn = new Button("вход");
        loginBtn.setTranslateX(-20);
        loginBtn.setTranslateY(50);
        loginBtn.setMinSize(100, 35);
        loginBtn.setOnAction(event -> {
            userName = loginField.getText();
            AuthMessage am = new AuthMessage(userName, passwordField.getText());
            nettyClient.sendMsg(am);
            closeModalWindow();
        });
        pane.getChildren().add(loginBtn);

        newWindow.setTitle("Авторизация");
        newWindow.setOnCloseRequest(event -> System.exit(0));
        newWindow.setResizable(false);
        newWindow.show();

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(() -> {
            try {
                nettyClient.run(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void refreshServer() {
        CommandMessage cm = new CommandMessage(REFRESH, null);
        nettyClient.sendMsg(cm);
    }

    public void refreshClient(TableView areaClient) {
        String[] str = data.getFilesNames(userName);
        areaClient.setItems(null);
        ObservableList<String> table = FXCollections.observableArrayList(str);
        areaClient.setItems(table);
    }

    public void closeModalWindow(){
        newWindow.close();
    }
}
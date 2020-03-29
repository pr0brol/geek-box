package client.gui;

import client.network.Data;
import client.network.NettyClient;
import common.AuthMessage;
import common.CommandMessage;
import common.FileMessage;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
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

    TableView<String> areaServer = new TableView<>();
    TableView<String> areaClient = new TableView<>();

    private AnchorPane mainPane;
    private AnchorPane regPane;

    private Scene mainScene;
    private Scene regScene;

    private Button downloadClient = new Button();
    private Button deleteClient = new Button();
    private Button refreshClient = new Button();

    private Button downloadServer = new Button();
    private Button deleteServer = new Button();
    private Button refreshServer = new Button();

    private Label labelClient = new Label();
    private Label labelServer = new Label();

    private Label loginLable = new Label();
    private Label passwordLabel = new Label();

    private TextField loginField;
    private PasswordField passwordField;

    private Button loginBtn = new Button();
    private Button cancelBtn = new Button();

    private static Stage regWindow;

    public Window(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    public TableView<String> getAreaServer() {
        return areaServer;
    }

    public void start(Stage primaryStage) {

        mainPane = new AnchorPane();
        regPane = new AnchorPane();

        mainScene = new Scene(mainPane, windowWidth, windowHeight);
        regScene = new Scene(regPane, 300, 150);

        data = new Data();

        primaryStage.setOnCloseRequest(event -> System.exit(0));

        setAreaMethod(areaClient, "Client_Path", 313.0, 314.0, 415.0, 30.0);
        AnchorPane.setLeftAnchor(areaClient, 5.0);

        setAreaMethod(areaServer, "Server_Path", 313.0, 314.0, 415.0, 30.0);
        AnchorPane.setRightAnchor(areaServer, 5.0);

        primaryStage.setTitle("geek-box");
        primaryStage.show();
        primaryStage.setResizable(false);
        primaryStage.setScene(mainScene);

        initButton(downloadClient, "загрузить файл", 100.0);
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

        initButton(deleteClient, "удалить файл", 100.0);
        deleteClient.setOnAction(event -> {
            String name = areaClient.getSelectionModel().getSelectedItem();
            if (name != null) {
                File file = new File(data.getPATH() + userName + "/" + name);
                file.delete();
                refreshClient(areaClient);
            }
        });

        initButton(refreshClient, "обновить", 100.0);
        refreshClient.setOnAction(event -> refreshClient(areaClient));

        initButton(downloadServer, "загрузить файл", 100.0);
        downloadServer.setOnAction(event -> {
            String name = areaServer.getSelectionModel().getSelectedItem();
            if (name != null) {
                CommandMessage cm = new CommandMessage(COPY, name);
                nettyClient.sendMsg(cm);
                refreshClient(areaClient);
            }
        });

        initButton(deleteServer, "удалить файл", 100.0);
        deleteServer.setOnAction(event -> {
            String name = areaServer.getSelectionModel().getSelectedItem();
            if (name != null) {
                CommandMessage cm = new CommandMessage(DELETE, name);
                nettyClient.sendMsg(cm);
                refreshServer();
            }
        });

        initButton(refreshServer, "обновить", 100.0);
        refreshServer.setOnAction(event -> refreshServer());

        setButtonInScene(mainPane, downloadClient, deleteClient, refreshClient, 5.0);
        AnchorPane.setLeftAnchor(downloadClient, 5.0);
        AnchorPane.setLeftAnchor(deleteClient, 112.0);
        AnchorPane.setLeftAnchor(refreshClient, 219.0);

        setButtonInScene(mainPane, downloadServer, deleteServer, refreshServer, 5.0);
        AnchorPane.setRightAnchor(refreshServer, 5.0);
        AnchorPane.setRightAnchor(deleteServer, 112.0);
        AnchorPane.setRightAnchor(downloadServer, 219.0);

        initMainLabel(labelClient, "Local Repository", 115, 5.0);
        AnchorPane.setLeftAnchor(labelClient, 5.0);

        initMainLabel(labelServer, "Cloud Repository", -112, 5.0);
        AnchorPane.setRightAnchor(labelServer, 5.0);

        initRegLabel(loginLable, "Логин", 50, 35, 10, 6);
        initRegLabel(passwordLabel, "Пароль", 50, 35, 10, 46);

        initLoginField();
        initPasswordField();

        initRegButton(loginBtn, "вход", 60, 100, 115, 35);
        loginBtn.setOnAction(event -> {
            userName = loginField.getText();
            AuthMessage am = new AuthMessage(userName, passwordField.getText());
            nettyClient.sendMsg(am);
            closeModalWindow();
        });

        initRegButton(cancelBtn, "отмена", 185, 100, 115, 35);
        cancelBtn.setOnAction(event -> System.exit(0));

        initRegWindow();

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(() -> {
            try {
                nettyClient.run(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void setButtonInScene(AnchorPane pane, Button dlBtn, Button dltBtn, Button refBtn, Double setBottom) {
        pane.getChildren().add(dlBtn);
        pane.getChildren().add(dltBtn);
        pane.getChildren().add(refBtn);
        AnchorPane.setBottomAnchor(dlBtn, setBottom);
        AnchorPane.setBottomAnchor(dltBtn, setBottom);
        AnchorPane.setBottomAnchor(refBtn, setBottom);
    }

    private void initMainLabel(Label lbl, String str, int setX, Double setTop) {
        lbl.setText(str);
        lbl.setTranslateX(setX);
        mainPane.getChildren().add(lbl);
        AnchorPane.setTopAnchor(lbl, setTop);
    }

    private void setAreaMethod(TableView tableView, String path, Double wc, Double w, Double h, Double top) {
        TableColumn<String, String> pathClientColumn = new TableColumn<>(path);
        pathClientColumn.setMinWidth(wc);
        pathClientColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        tableView.getColumns().add(pathClientColumn);
        tableView.setEditable(false);
        tableView.setMinWidth(w);
        tableView.setMinHeight(h);
        mainPane.getChildren().add(tableView);
        AnchorPane.setTopAnchor(tableView, top);
    }

    private void initButton(Button btn, String str, Double width) {
        btn.setText(str);
        btn.setMinWidth(width);
    }

    private void initRegWindow() {
        regWindow = new Stage();
        regWindow.initModality(Modality.WINDOW_MODAL);
        regWindow.initOwner(mainScene.getWindow());
        regWindow.setOnCloseRequest(event -> System.exit(0));
        regWindow.setScene(regScene);
        regWindow.setResizable(false);
        regWindow.show();
    }

    private void initRegLabel(Label lbl, String str, int w, int h, int setX, int setY) {
        lbl.setText(str);
        lbl.setMinSize(w, h);
        lbl.setTranslateX(setX);
        lbl.setTranslateY(setY);
        regPane.getChildren().add(lbl);
    }

    private void initLoginField() {
        loginField = new TextField();
        loginField.setMinSize(240, 20);
        loginField.setTranslateX(60);
        loginField.setTranslateY(10);
        regPane.getChildren().add(loginField);
    }

    private void initPasswordField() {
        passwordField = new PasswordField();
        passwordField.setMinSize(240, 20);
        passwordField.setTranslateX(60);
        passwordField.setTranslateY(50);
        regPane.getChildren().add(passwordField);
    }

    private void initRegButton(Button btn, String str, int setX, int setY, int w, int h) {
        btn.setText(str);
        btn.setTranslateX(setX);
        btn.setTranslateY(setY);
        btn.setMinSize(w, h);
        regPane.getChildren().add(btn);
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

    public static void closeModalWindow() {
        regWindow.hide();
    }
}
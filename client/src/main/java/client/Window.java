package client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import static javafx.geometry.Pos.BASELINE_CENTER;

public class Window extends Application {
    private int windowHeight = 480;
    private int windowWidth = 640;



    @Override
    public void start(Stage primaryStage) throws Exception {

        AnchorPane root = new AnchorPane();

        Scene scene = new Scene(root, windowWidth, windowHeight);

        primaryStage.setTitle("geek-box");
        primaryStage.show();
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);

        Button downloadClient = new Button("загрузить файл");
        downloadClient.setMinWidth(100.0);
        downloadClient.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "загрузка");
            alert.showAndWait();
        });

        Button deleteClient = new Button("удалить файл");
        deleteClient.setMinWidth(100.0);
        deleteClient.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "удаление");
            alert.showAndWait();
        });

        Button refreshClient = new Button("обновить");
        refreshClient.setMinWidth(100.0);
        refreshClient.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "обновление");
            alert.showAndWait();
        });

        Button downloadServer = new Button("загрузить файл");
        downloadServer.setMinWidth(100.0);
        downloadServer.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "загрузка");
            alert.showAndWait();
        });

        Button deleteServer = new Button("удалить файл");
        deleteServer.setMinWidth(100.0);
        deleteServer.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "удаление");
            alert.showAndWait();
        });

        Button refreshServer = new Button("обновить");
        refreshServer.setMinWidth(100.0);
        refreshServer.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "обновление");
            alert.showAndWait();
        });


        root.getChildren().add(downloadClient);
        root.getChildren().add(deleteClient);
        root.getChildren().add(refreshClient);
        AnchorPane.setBottomAnchor(downloadClient, 5.0);     // хотел организовать смещение кнопок используя размер downloadClient.getWidth()
        AnchorPane.setLeftAnchor(downloadClient, 5.0);      // но он почему то выдаёт 0.0
        AnchorPane.setLeftAnchor(deleteClient, 112.0);
        AnchorPane.setBottomAnchor(deleteClient, 5.0);
        AnchorPane.setLeftAnchor(refreshClient, 219.0);
        AnchorPane.setBottomAnchor(refreshClient, 5.0);

        root.getChildren().add(downloadServer);
        root.getChildren().add(deleteServer);
        root.getChildren().add(refreshServer);
        AnchorPane.setBottomAnchor(refreshServer, 5.0);     // хотел организовать смещение кнопок используя размер downloadClient.getWidth()
        AnchorPane.setRightAnchor(refreshServer, 5.0);      // но он почему то выдаёт 0.0
        AnchorPane.setRightAnchor(deleteServer, 112.0);
        AnchorPane.setBottomAnchor(deleteServer, 5.0);
        AnchorPane.setRightAnchor(downloadServer, 219.0);
        AnchorPane.setBottomAnchor(downloadServer, 5.0);

        Label labelClient = new Label("Local Repository");
        labelClient.setAlignment(BASELINE_CENTER);          //не получается поставить в середине
        root.getChildren().add(labelClient);
        AnchorPane.setTopAnchor(labelClient, 5.0);
        AnchorPane.setLeftAnchor(labelClient, 5.0);

        Label labelServer = new Label("Cloud Repository");
        labelServer.setAlignment(BASELINE_CENTER);          //то же самое
        root.getChildren().add(labelServer);
        AnchorPane.setTopAnchor(labelServer, 5.0);
        AnchorPane.setRightAnchor(labelServer, 5.0);

        TextArea areaClient = new TextArea();
        areaClient.setMaxWidth(314);
        areaClient.setMinHeight(415);
        root.getChildren().add(areaClient);
        AnchorPane.setLeftAnchor(areaClient, 5.0);
        AnchorPane.setTopAnchor(areaClient, 30.0);

        TextArea areaServer = new TextArea();
        areaServer.setMaxWidth(314);
        areaServer.setMinHeight(415);
        root.getChildren().add(areaServer);
        AnchorPane.setRightAnchor(areaServer, 5.0);
        AnchorPane.setTopAnchor(areaServer, 30.0);
    }
}
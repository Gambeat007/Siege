package pw.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

public class ChoiceController {
    public StartController startController;
    @FXML
    public HBox boxes;
    VBox chosenVBox;
    Socket socket;
    String army = "goblin";

    public ChoiceController(StartController startController) {
        this.startController = startController;
    }

    @FXML
    public void initialize() {
        try {
            socket = new Socket("localhost", 9001);
            System.out.println("Socket is connected with server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleChoice(MouseEvent event) {
        chosenVBox = (VBox) event.getSource();

        for (int i = 0; i < boxes.getChildren().toArray().length; i++) {
            VBox vbox = (VBox) boxes.getChildren().toArray()[i];
            if (chosenVBox.getId().equals(vbox.getId())) {
                chosenVBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                army = chosenVBox.getId();
            } else {
                vbox.setBorder(null);
            }
        }
    }

    public void handlePlay() {
        changeWindow();
    }

    public void changeWindow() {
        try {
            Stage stage = (Stage) chosenVBox.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("MainWindow.fxml"));
            MainController mainController = new MainController(this, army, stage);
            fxmlLoader.setController(mainController);
            Parent root = fxmlLoader.load();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
            stage.setTitle(startController.getUsername() + "");
            stage.setOnCloseRequest(event -> {
                System.exit(0);
            });
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return this.socket;
    }

}

package pw.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;


public class StartController {

    @FXML public TextField userName;

    public static String username;

    public void handleJoin() {
        username = userName.getText();

        changeWindow();
    }

    public void changeWindow() {
        try {
            Stage stage = (Stage) userName.getScene().getWindow();

            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("ChoiceWindow.fxml"));
            ChoiceController choiceController = new ChoiceController(this);
            fxmlLoader.setController(choiceController);
            Parent root = fxmlLoader.load();
            stage.setScene(new Scene(root));
            stage.setTitle(username + "");
            stage.setOnCloseRequest(event -> {
                System.exit(0);
            });
            stage.setResizable(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }
}
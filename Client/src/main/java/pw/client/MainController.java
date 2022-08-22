package pw.client;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController extends Thread implements Initializable {

    @FXML public VBox vbox01;
    @FXML public TextField topTextField;
    @FXML public Label statHealthPoints;
    @FXML public Label statMoveRadius;
    @FXML public Label statAttackDamage;
    @FXML public Label statAttackRange;


    //chat
    @FXML public TextField chatTextField;
    @FXML public TextArea chatTextArea;

    @FXML public AnchorPane boardPane;

    BufferedReader reader;
    PrintWriter writer;
    Socket socket;
    ChoiceController choiceController;

    Board board;
    String army;
    private Hexagon[][] hexagons;
    int maxI = 9;
    int maxJ = 13;
    String currentPort;

    Stage stage;
    HBox hbox;
    Polygon hex;


    public MainController(ChoiceController choiceController, String army, Stage stage) {
        this.choiceController = choiceController;
        this.army = army;
        this.stage = stage;

        hexagons = new Hexagon[maxI][maxJ];

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            socket = choiceController.getSocket();
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

        } catch (IOException e) {
            e.printStackTrace();
        }



        for(int i = 0; i < vbox01.getChildren().toArray().length; i++) {
            hbox = (HBox) vbox01.getChildren().toArray()[i];
            for (int j = 0; j < hbox.getChildren().toArray().length; j++) {
                hex = (Polygon) hbox.getChildren().toArray()[j];
                hexagons[i][j] = new Hexagon(hex, i, j);
            }
        }

        board = new Board(hexagons);



        boardPane.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                Double yProportion = t1.doubleValue()/870;

                board.yResizeAll(yProportion);
            }
        });

        boardPane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                Double xProportion = t1.doubleValue()/1393;

                board.xResizeAll(xProportion);
            }
        });

        send("first:" + army);
        this.start();

    }

    @Override
    public void run() {
        try {
            Thread.sleep(300);
            while (true) {
                String msg = reader.readLine();
                System.out.println("reader: " + msg);
                String[] tokens = msg.split(":");

                if (tokens[0].equals("endgame")) {
                    endgame(tokens[1]);
                } else if (tokens[0].equals("chat")) {
                    String[] msgTokens = tokens[2].split(" ");
                    String cmd = msgTokens[0];
                    System.out.println(cmd);
                    StringBuilder fulmsg = new StringBuilder();
                    for(int i = 1; i < msgTokens.length; i++) {
                        fulmsg.append(msgTokens[i] + " ");
                    }
                    if (cmd.equalsIgnoreCase(StartController.username + ":")) {
                        continue;
                    }
                    chatTextArea.appendText( tokens[1] + ": " + fulmsg + "\n");

                } else {
                    String[] hexes = tokens[1].split(";");
                    currentPort = tokens[0];
                    board.deactivateAll();
                    board.parseMessage(hexes, socket.getLocalPort());
                    displayCurrentPlayer();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void hexOnClicked(MouseEvent event) {
        Polygon source = (Polygon) event.getSource();

        if (currentPort.equals("" + socket.getLocalPort())) {
            if (board.isAnyHexActive()) {
                if (board.isFilledIn(source)) {
                    if(board.isOpponent(currentPort, source) && board.isShootingActiveNeighbour(source)) {

                        send(StartController.username + ":" + board.takeAction("attack", (Polygon) event.getSource()));
                        board.deactivateAll();
                        System.out.println("attack");
                    } else {
                        board.deactivateAll();
                        board.activate(currentPort, source);
                    }
                } else {
                    if (board.isActiveNeighbour(source)) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            System.out.println("move");
                            send(StartController.username + ":" + board.takeAction("move", (Polygon) event.getSource()));
                        } else if (event.getButton() == MouseButton.SECONDARY && board.canBuild() && board.isShootingActiveNeighbour(source)) {
                            System.out.println("build");
                            send(StartController.username + ":" + board.takeAction("build", (Polygon) event.getSource()));
                        }

                    } else {
                        board.deactivateAll();
                    }
                }
            } else if (board.isFilledIn(source)) {
                board.activate(currentPort, source);
            }
        }
    }

    public void sendChatMessage() {
        String msg = chatTextField.getText();
        writer.println("chat:" + StartController.username + ": " + msg);
        chatTextField.setText("");
    }

    public void moreInfo(MouseEvent event) {
        Polygon source = (Polygon) event.getSource();

        String[] stats = board.getUnitStats(source);

        String healthPoints = stats[0];
        String moveRadius = stats[1];
        String attackDamage = stats[2];
        String attackRange = stats[3];

        statHealthPoints.setText(healthPoints);
        statMoveRadius.setText(moveRadius);
        statAttackDamage.setText(attackDamage);
        statAttackRange.setText(attackRange);
        
    }

    public void hexOnRightClicked(MouseEvent event) {
        Polygon source = (Polygon) event.getSource();

        if (currentPort.equals("" + socket.getLocalPort())) {
            if (board.isAnyHexActive()) {
                if (!board.isFilledIn(source)) {
                    if (board.isActiveNeighbour(source)) {
                        System.out.println("build");
                        send(StartController.username + ":" + board.takeAction("build", (Polygon) event.getSource()));
                        board.deactivateAll();
                    } else {
                        board.deactivateAll();
                    }
                }
            } else if (board.isFilledIn(source)) {
                board.activate(currentPort, source);
            }
        }
    }


    public void send(String msg) {
        writer.println(msg);
    }

    private void endgame(String winner) {
        System.out.println("the game has ended");

        Platform.runLater(() -> {
            try {
                Stage stage = (Stage) vbox01.getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("EndWindow.fxml"));
                EndController endController = new EndController(winner, socket.getLocalPort() + "");
                fxmlLoader.setController(endController);
                Parent root = fxmlLoader.load();
                stage.setScene(new Scene(root));
                stage.setTitle(choiceController.startController.getUsername() + "");

                stage.setOnCloseRequest(event -> {
                    System.exit(0);
                });
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    public void displayCurrentPlayer() {
        if (currentPort.equals("" + socket.getLocalPort())) {
            String message = "Welcome " + StartController.username + "! Now is your turn.";
            topTextField.setText(message);
        } else {
            String message = "Welcome " + StartController.username + "! Now is your opponent turn.";
            topTextField.setText(message);
        }
    }
}

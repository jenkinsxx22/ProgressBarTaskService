package application;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    static Task taskWorker;
    final int numFiles = 30;

    public static void main(String[] args) {
        Application.launch(args);//  ww  w .  j  a  v a  2s .  c  o m
    }
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Background Processes");
        Group root = new Group();
        Scene scene = new Scene(root, 330, 120, Color.WHITE);

        BorderPane mainPane = new BorderPane();
        mainPane.layoutXProperty().bind(scene.widthProperty().subtract(mainPane.widthProperty()).divide(2));
        root.getChildren().add(mainPane);

        final Label label = new Label("Files Transfer:");
        final ProgressBar progressBar = new ProgressBar(0);
        final ProgressIndicator progressIndicator = new ProgressIndicator(0);

        final HBox hb = new HBox();
        hb.setSpacing(5);
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().addAll(label, progressBar, progressIndicator);
        mainPane.setTop(hb);

        final Button startButton = new Button("Start");
        final Button cancelButton = new Button("Cancel");
        final TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setPrefSize(200, 70);
        final HBox hb2 = new HBox();
        hb2.setSpacing(5);
        hb2.setAlignment(Pos.CENTER);
        hb2.getChildren().addAll(startButton, cancelButton, textArea);
        mainPane.setBottom(hb2);

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                startButton.setDisable(true);
                progressBar.setProgress(0);
                progressIndicator.setProgress(0);
                textArea.setText("");
                cancelButton.setDisable(false);
                taskWorker = createWorker(numFiles);

                // wire up progress bar
                progressBar.progressProperty().unbind();
                progressBar.progressProperty().bind(taskWorker.progressProperty());
                progressIndicator.progressProperty().unbind();
                progressIndicator.progressProperty().bind(taskWorker.progressProperty());
                // append to text area box
                taskWorker.messageProperty().addListener(new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        textArea.appendText(newValue + "\n");
                    }
                });
                new Thread(taskWorker).start();
            }
        });
        // cancel button will kill worker and reset.
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                startButton.setDisable(false);
                cancelButton.setDisable(true);
                taskWorker.cancel(true);
                // reset
                progressBar.progressProperty().unbind();
                progressBar.setProgress(0);
                progressIndicator.progressProperty().unbind();
                progressIndicator.setProgress(0);
                textArea.appendText("File transfer was cancelled.");
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public Task createWorker(final int numFiles) {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                for (int i = 0; i < numFiles; i++) {
                    long elapsedTime = System.currentTimeMillis();
                    Thread.sleep(1000);
                    elapsedTime = System.currentTimeMillis() - elapsedTime;
                    String status = elapsedTime + " milliseconds";
                    updateMessage(status);
                    updateProgress(i + 1, numFiles);
                }
                return true;
            }
        };
    }    
}

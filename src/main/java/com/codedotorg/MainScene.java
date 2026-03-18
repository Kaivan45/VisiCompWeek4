package com.codedotorg;

import com.codedotorg.modelmanager.CameraController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class MainScene {
    
    private VBox rootLayout;
    private ImageView cameraView;
    private Label titleLabel;
    private Label computerChoiceLabel;
    private Label predictionLabel;
    private Label promptLabel;
    private Button exitButton;
    private Loading cameraLoading;
    private boolean firstCapture;

    private int counter = 3;
    private boolean isCounting = false;

    /**
     * FUNGSI BARU: Menampilkan hasil akhir ke layar
     * Panggil ini dari RockPaperScissors.java atau App.java
     */
    public void updateGameResult(String resultText) {
        Platform.runLater(() -> {
            // Kita gunakan promptLabel atau titleLabel untuk pamer hasil agar terlihat besar
            promptLabel.setText(resultText); 
            System.out.println("Tampilan Layar: " + resultText);
        });
    }

    public void startCountdown() {
        if (isCounting) return;
        isCounting = true;

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (counter > 0) {
                Platform.runLater(() -> promptLabel.setText("SIAP-SIAP: " + counter));
                counter--;
            } else {
                Platform.runLater(() -> promptLabel.setText("GO!"));
                isCounting = false;
                counter = 3;
            }
        }));
        
        timeline.setCycleCount(4);
        timeline.play();
    }

    public MainScene() {
        cameraView = new ImageView();
        cameraView.setId("camera");

        exitButton = new Button("Exit");
        
        titleLabel = new Label("Rock, Paper, Scissors");
        titleLabel.setId("titleLabel");

        computerChoiceLabel = new Label("");
        predictionLabel = new Label("");
        promptLabel = new Label("Make your choice!"); // Ini yang akan kita update

        cameraLoading = new Loading();  
        firstCapture = true; 
    }

    public Scene createMainScene(CameraController cameraController) {
        createExitButtonAction(cameraController);

        rootLayout = new VBox();
        rootLayout.setAlignment(Pos.CENTER);

        Region cameraSpacer1 = createSpacer(20);
        Region cameraSpacer2 = createSpacer(20);
        Region buttonSpacer = createSpacer(10);

        rootLayout.getChildren().addAll(titleLabel, promptLabel, cameraLoading.getCameraLoadingLabel(),
            cameraSpacer1, cameraView, cameraSpacer2, cameraLoading.getProgressIndicator(), 
            computerChoiceLabel, predictionLabel, buttonSpacer, exitButton);

        if (!isFirstCapture()) {
            cameraLoading.hideLoadingAnimation(rootLayout, cameraView);
        } else {
            setFirstCaptureFalse();
        }

        Scene mainScene = new Scene(rootLayout, 600, 750);
        mainScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        return mainScene;
    }

    public void showUserResponse(String predictedClass, double predictedScore) {
        cameraLoading.hideLoadingAnimation(rootLayout, cameraView);
        
        // Membersihkan nama label (misal "0 rock" jadi "rock")
        String user = predictedClass.contains(" ") ? 
                       predictedClass.substring(predictedClass.indexOf(" ") + 1) : 
                       predictedClass;

        int percentage = (int)(predictedScore * 100);
        String userResult = "User: " + user + " (" + percentage + "%)";

        Platform.runLater(() -> predictionLabel.setText(userResult));
    }

    // ... (Fungsi helper lainnya tetap sama)
    public ImageView getCameraView() { return cameraView; }
    public Loading getLoadingAnimation() { return cameraLoading; }
    public boolean isFirstCapture() { return firstCapture; }
    public void setFirstCaptureFalse() { this.firstCapture = false; }
    private Region createSpacer(double amount) {
        Region temp = new Region();
        temp.setPrefHeight(amount);
        return temp;
    }
    private void createExitButtonAction(CameraController cameraController) {
        exitButton.setOnAction(event -> {
            cameraController.stopCapture();
            System.exit(0);
        });
    }
}
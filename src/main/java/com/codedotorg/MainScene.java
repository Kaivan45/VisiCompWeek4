package com.codedotorg;

import com.codedotorg.modelmanager.CameraController;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class MainScene {
    private VBox rootLayout;
    private ImageView cameraView;
    private Label titleLabel, computerChoiceLabel, predictionLabel, promptLabel;
    private Button exitButton;
    private Loading cameraLoading;
    private boolean firstCapture = true;

    public MainScene() {
        cameraView = new ImageView();
        cameraView.setId("camera"); // PENTING: ID untuk CSS dan Model
        cameraView.setFitWidth(400);
        cameraView.setPreserveRatio(true);
        cameraView.getStyleClass().add("camera-view");

        titleLabel = new Label("AI ROCK PAPER SCISSORS");
        titleLabel.getStyleClass().add("title-text");

        promptLabel = new Label("Tunjukkan Tanganmu!");
        promptLabel.getStyleClass().add("prompt-text");

        computerChoiceLabel = new Label("KOMPUTER: MENUNGGU...");
        computerChoiceLabel.getStyleClass().add("status-label");

        predictionLabel = new Label("ANDA: -");
        predictionLabel.getStyleClass().add("status-label");

        exitButton = new Button("KELUAR");
        exitButton.getStyleClass().add("exit-button");

        // Buat objek loading di sini
        cameraLoading = new Loading();
    }

    public Scene createMainScene(CameraController cameraController) {
        createExitButtonAction(cameraController);

        rootLayout = new VBox(20);
        rootLayout.setAlignment(Pos.CENTER);
        rootLayout.setPadding(new Insets(30));
        rootLayout.getStyleClass().add("main-background");

        VBox header = new VBox(10, titleLabel, promptLabel);
        header.setAlignment(Pos.CENTER);

        VBox centerArea = new VBox(15, cameraView, computerChoiceLabel, predictionLabel);
        centerArea.setAlignment(Pos.CENTER);

        rootLayout.getChildren().clear();
        rootLayout.getChildren().addAll(header, centerArea, exitButton);

        // Jika ini bukan pertama kali (reset game), sembunyikan loading
        if (!firstCapture) {
            cameraLoading.hideLoadingAnimation(rootLayout, cameraView);
        }

        Scene scene = new Scene(rootLayout, 650, 850);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        
        return scene;
    }

    // FUNGSI KRITIS: Dipanggil dari RockPaperScissors untuk mematikan loading
    public void stopLoading() {
        Platform.runLater(() -> {
            cameraLoading.hideLoadingAnimation(rootLayout, cameraView);
            firstCapture = false;
        });
    }

    public void updateGameResult(String text) { Platform.runLater(() -> promptLabel.setText(text)); }
    public void showComputerChoice(String choice) {
        Platform.runLater(() -> computerChoiceLabel.setText("KOMPUTER: " + choice.toUpperCase()));
    }
    public void showUserResponse(String res, double score) {
        Platform.runLater(() -> predictionLabel.setText("ANDA: " + res.toUpperCase()));
    }
    
    public ImageView getCameraView() { return cameraView; }
    public Loading getLoadingAnimation() { return cameraLoading; }
    
    private void createExitButtonAction(CameraController cc) { 
        exitButton.setOnAction(e -> { cc.stopCapture(); System.exit(0); }); 
    }
}
package com.codedotorg;

import com.codedotorg.modelmanager.CameraController;
import com.codedotorg.modelmanager.ModelManager;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;

public class RockPaperScissors {

    /** The main window of the app */
    private Stage window;

    /** The MainScene of the game */
    private MainScene game;

    /** The GameOverScene to display the winner */
    private GameOverScene gameOver;

    /** The GameLogic to handle the game logic */
    private GameLogic logic;

    /** Manages the TensorFlow model used for image classification */
    private ModelManager model;

    /** Controls the camera capture and provides frames to the TensorFlow model for classification */
    private CameraController cameraController;

    /** The Timeline to manage how often a prediction is made */
    private Timeline timeline;

    private String lastDetectedClass = "";
    private int detectionCounter = 0;
    private final int CONFIRMATION_THRESHOLD = 3; // Berapa kali deteksi harus sama (3x ≈ 1.5 - 2 detik)

    /**
     * Constructor for the RockPaperScissors class.
     * Sets up the window using the primaryStage, initializes the model
     * and camera capture, sets up the game scenes and logic.
     *
     * @param primaryStage the primary stage for the application
     */
    public RockPaperScissors(Stage primaryStage) {
        // Set up the window using the primaryStage
        setUpWindow(primaryStage);

        // Set up the model and camera capture
        cameraController = new CameraController();
        model = new ModelManager();

        // Set up the game scenes and logic
        game = new MainScene();
        gameOver = new GameOverScene();
        logic = new GameLogic();
    }

    /**
     * Sets up the window with the given primaryStage, sets the title of
     * the window to "Rock, Paper, Scissors", and adds a shutdown hook to
     * stop the camera capture when the app is closed.
     *
     * @param primaryStage the primary stage of the application
     */
    public void setUpWindow(Stage primaryStage) {
        // Set window to point to the primaryStage
        window = primaryStage;

        // Set the title of the window
        window.setTitle("Rock, Paper, Scissors");

        // Shutdown hook to stop the camera capture when the app is closed
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            cameraController.stopCapture();
        }));
    }
    
    /**
     * Starts a new game of Rock Paper Scissors by loading the main
     * screen and updating the game state.
     */
    public void playGame() {
        loadMainScreen();
        updateGame();
    }

    /**
     * Loads the main screen of the game, setting it to starting defaults
     * and displaying the window. Captures the camera view and sets the model
     * for the cameraController object. Retrieves the Loading object and
     * shows the loading animation while the camera is loading.
     */
    public void loadMainScreen() {
        // Set the game to starting defaults
        resetGame();

        // Display the window
        window.show();

        // Capture the camera view and set the model for the cameraController object
        cameraController.captureCamera(game.getCameraView(), model);

        // Retrieve the Loading object
        Loading cameraLoading = game.getLoadingAnimation();

        // Show the loading animation while the camera is loading
        cameraLoading.showLoadingAnimation(game.getCameraView());
    }

    /**
     * Updates the game by getting the predicted class and score from
     * the CameraController, showing the user's response and confidence
     * score in the app, getting the computer's choice, getting the
     * winner, and loading the game over screen if the game is over.
     */
    public void updateGame() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
            String currentPrediction = cameraController.getPredictedClass();
            
            // MATIKAN LOADING: Begitu ada prediksi (meskipun neutral), matikan loading
            if (currentPrediction != null) {
                game.stopLoading(); 
            }

            if (currentPrediction != null && !currentPrediction.toLowerCase().contains("neutral")) {
                // Logika konfirmasi tangan (seperti sebelumnya)
                if (currentPrediction.equals(lastDetectedClass)) {
                    detectionCounter++;
                    game.updateGameResult("MENGUNCI: " + currentPrediction.toUpperCase() + " (" + detectionCounter + "/3)");
                } else {
                    lastDetectedClass = currentPrediction;
                    detectionCounter = 1;
                }

                if (detectionCounter >= 3) {
                    timeline.pause();
                    prosesHasilRonde(currentPrediction);
                }
            } else {
                detectionCounter = 0;
                game.updateGameResult("Ayo, tunjukkan tanganmu!");
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void prosesHasilRonde(String userChoice) {
        String computerMove = logic.getComputerChoice();
        game.showUserResponse(userChoice, 0);
        game.showComputerChoice(computerMove);
        
        String result = logic.determineWinner(userChoice, computerMove);
        game.updateGameResult(result);

        PauseTransition pause = new PauseTransition(Duration.seconds(4));
        pause.setOnFinished(e -> {
            if (logic.isGameOver()) {
                loadGameOver(result);
            } else {
                game.showComputerChoice("MENUNGGU...");
                game.updateGameResult("SIAP UNTUK RONDE BERIKUTNYA?");
                detectionCounter = 0;
                timeline.play();
            }
        });
        pause.play();
    }

    /**
     * Loads the Game Over scene with the winner's name and sets the
     * playAgainButton to reset the game when clicked. Stops the timeline.
     *
     * @param winner the name of the winner of the game
     */
    public void loadGameOver(String winnerText) {
        // Ambil statistik lengkap dari logic
        String finalStats = logic.getFinalStats();

        Button playAgainButton = gameOver.getPlayAgainButton();
        playAgainButton.setOnAction(event -> {
            resetGame();
        });

        // Kirim statistik ke layar Game Over
        Scene gameOverScene = gameOver.createGameOverScene(finalStats, cameraController);

        window.setScene(gameOverScene);
        timeline.stop();
    }

    /**
     * Resets the game by resetting the game logic, creating a new main scene,
     * and setting the window to display the new scene. If a timeline is currently
     * running, it is resumed.
     */
    public void resetGame() {
        // Reset the GameLogic
        logic.resetLogic();

        // Create the MainScene for the game
        Scene mainScene = game.createMainScene(cameraController);

        // Set the MainScene in the window
        window.setScene(mainScene);
        
        // Play the timeline if it is not null
        if (timeline != null) {
            timeline.play();
        }
    }

    

}

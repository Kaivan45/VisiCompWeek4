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
        timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
            // Get the predicted class and score from the CameraController
            String predictedClass = cameraController.getPredictedClass();
            double predictedScore = cameraController.getPredictedScore();

            if (predictedClass != null) {
                // Show the user's response and confidence score in the app
                game.showUserResponse(predictedClass, predictedScore);

                // Get the computer's choice
                String computerChoice = logic.getComputerChoice();

                // Get the winner
                String winner = logic.determineWinner(predictedClass, computerChoice);
                
                if (logic.isGameOver()) {
                    // Create a pause transition of 3 seconds
                    PauseTransition pause = new PauseTransition(Duration.seconds(3));

                    // Set the action to execute after the pause
                    pause.setOnFinished(e -> loadGameOver(winner));

                    // Start the pause transition
                    pause.play();
                }
            }
        }));

        // Specify that the animation should repeat indefinitely
        timeline.setCycleCount(Timeline.INDEFINITE);

        // Start the animation
        timeline.play();
    }

    /**
     * Loads the Game Over scene with the winner's name and sets the
     * playAgainButton to reset the game when clicked. Stops the timeline.
     *
     * @param winner the name of the winner of the game
     */
    public void loadGameOver(String winner) {
        // Retrieve the playAgainButton from the GameOverScene
        Button playAgainButton = gameOver.getPlayAgainButton();

        // Set the playAgainButton to reset the game when clicked
        playAgainButton.setOnAction(event -> {
            resetGame();
        });

        // Create the GameOverScene layout
        Scene gameOverScene = gameOver.createGameOverScene(winner, cameraController);

        // Set the GameOverScene in the window
        window.setScene(gameOverScene);

        // Stop the timeline
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

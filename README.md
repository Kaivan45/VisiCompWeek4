# Rock, Paper, Scissors

This application is a rock, paper, scissors game where the user plays against the computer. The computer makes its choice randomly, and the winner is determined based on the traditional rules of the game.

## 🔎 Existing Code

### RockPaperScissors.java

The `RockPaperScissors` class orchestrates the game's flow, providing the framework for executing the game's logic, managing the camera and model interactions, and controlling the scene transitions. It incorporates the main game view, the end game view, and transitions between them based on the game's outcome.

**Suggested Modifications and New Features:**

* Introduce multiple rounds within a game session, tallying scores before declaring a final winner.

### MainScene.java

The `MainScene` class manages the main interface where the game of Roc, Paper, Scissors is played. It displays the camera feed, the app's title, the computer's choice, the user's predicted gesture, and the corresponding confidence score. This class also provides feedback to the user in the form of text labels, spacers, and a loading animation during camera initialization.

**Suggested Modifications and New Features:**

* Add a countdown before capturing the user's gesture to give the user some time to prepare.
* Incorporate audio cues or background music to enhance the user experience.

### GameOverScene.java

The `GameOverScene` class manages the game-over screen's interface and interactions. It presents the result of the game, allows the user to play again or exit the application, and manages the app's shutdown.

**Suggested Modifications and New Features:**

* Enhance the display by showing statistics like the number of games won, lost, or tied.
* Add sound effects or background music to enhance the user experience.

### Loading.java

The `Loading` class manages the loading animations and labels shown during camera initialization or data processing.

**Suggested Modifications and New Features:**

* Introduce different loading animations for variety.
* Display a loading percentage or estimated time remaining.
* Implement a retry or cancel mechanism for prolonged loading times.

## ✅ TO DO: GameLogic.java

The `GameLogic` class provides a framework for managing the logic of a Rock, Paper, Scissors game, such as determining the winner, handling game state, and generating computer choices.

Implement the following methods in `GameLogic.java` to incorporate your Teachable Machine model in the app.

`getComputerChoice()`

* Randomly select one of the three options: "rock", "paper", or "scissors" for the computer player.
* Return the randomly chosen string.

`determineWinner(String predictedClass, String computerChoice)`

* Based on the user's predicted gesture (`predictedClass`) and the computer's random choice (`computerChoice`), decide the outcome of the game round.
* Consider all combinations (rock vs paper, rock vs scissors, etc.) and use the game rules to determine the winner.
* Return a string that combines both choices and specifies the round's outcome.

`getTieResult()`

* Implement functionality to handle a tie result. This method is triggered when both the user and the computer make the same choice.
* Set the `gameOver` flag to `true`.
* Return an appropriate string indicating the game has tied.

`getUserWinnerResult()`

* Implement functionality to handle when the user wins.
* Set the `gameOver` flag to `true`.
* Return a string indicating that the user has won the game.

`getComputerWinnerResult()`

* Implement functionality to handle when the computer wins.
* Set the `gameOver` flag to `true`.
* Return a string indicating that the computer has won the game.

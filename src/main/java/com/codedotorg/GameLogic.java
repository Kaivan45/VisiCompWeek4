package com.codedotorg;

import java.util.Random;

public class GameLogic {

    private boolean gameOver;
    private Random rand;
    private int userScore = 0;
    private int computerScore = 0;

    public GameLogic() {
        this.gameOver = false;
        this.rand = new Random(); 
    }

    public String getComputerChoice() {
        String[] choices = {"rock", "paper", "scissors"};
        return choices[rand.nextInt(choices.length)];
    }

    public String determineWinner(String predictedClass, String computerChoice) {
        String user = predictedClass.toLowerCase().trim();
        String computer = computerChoice.toLowerCase().trim();

        // Jika yang terdeteksi adalah neutral, jangan hitung skor
        if (user.contains("neutral")) {
            return "Tangan tidak terlihat...";
        }

        // Logika Seri
        if (user.contains(computer) || computer.contains(user)) {
            return "SERI! [" + userScore + " - " + computerScore + "]";
        }

        // Logika User Menang
        if ((user.contains("rock") && computer.equals("scissors")) ||
            (user.contains("paper") && computer.equals("rock")) ||
            (user.contains("scissors") && computer.equals("paper"))) {
            userScore++;
            return "KAMU MENANG! [" + userScore + " - " + computerScore + "]";
        }

        // Logika Komputer Menang
        computerScore++;
        return "KOMPUTER MENANG! [" + userScore + " - " + computerScore + "]";
    }

    public boolean isGameOver() { return gameOver; }
    public void resetLogic() { this.gameOver = false; }
}
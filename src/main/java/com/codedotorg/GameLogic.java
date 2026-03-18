package com.codedotorg;

import java.util.Random;

public class GameLogic {

    private boolean gameOver;
    private Random rand;

    /**
     * Constructor untuk GameLogic.
     */
    public GameLogic() {
        this.gameOver = false;
        this.rand = new Random(); 
    }

    /**
     * Memilih "rock", "paper", atau "scissors" secara acak.
     */
    public String getComputerChoice() {
        String[] choices = {"rock", "paper", "scissors"};
        int randomIndex = rand.nextInt(choices.length);
        return choices[randomIndex];
    }

    /**
     * Menentukan pemenang. 
     * Menggunakan .contains() agar cocok dengan label "0 rock", "1 paper", dsb.
     */
    public String determineWinner(String predictedClass, String computerChoice) {
        // 1. Bersihkan input (huruf kecil dan hapus spasi)
        String user = predictedClass.toLowerCase().trim();
        String computer = computerChoice.toLowerCase().trim();

        // Debugging di terminal VS Code untuk melihat apa yang ditangkap kamera
        System.out.println("DEBUG -> User: [" + user + "] vs Computer: [" + computer + "]");

        // 2. Cek apakah seri
        // (Misal user "0 rock" dan computer "rock", maka dianggap seri)
        if (user.contains(computer) || computer.contains(user)) {
            return "User: " + user + " | Comp: " + computer + " → " + getTieResult();
        }

        // 3. Logika User Menang
        if (
            (user.contains("rock") && computer.equals("scissors")) ||
            (user.contains("paper") && computer.equals("rock")) ||
            (user.contains("scissors") && computer.equals("paper"))
        ) {
            return "User: " + user + " | Comp: " + computer + " → " + getUserWinnerResult();
        }

        // 4. Logika Komputer Menang
        if (
            (computer.equals("rock") && user.contains("scissors")) ||
            (computer.equals("paper") && user.contains("rock")) ||
            (computer.equals("scissors") && user.contains("paper"))
        ) {
            return "User: " + user + " | Comp: " + computer + " → " + getComputerWinnerResult();
        }

        // 5. Jika label tidak dikenal (misal "neutral" atau "background")
        return "User: " + user + " | Comp: " + computer + " → Waiting for valid gesture...";
    }

    public String getTieResult() {
        this.gameOver = true;
        return "It's a Tie!";
    }

    public String getUserWinnerResult() {
        this.gameOver = true;
        return "You Win!";
    }

    public String getComputerWinnerResult() {
        this.gameOver = true;
        return "Computer Wins!";
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void resetLogic() {
        this.gameOver = false;
    }
}
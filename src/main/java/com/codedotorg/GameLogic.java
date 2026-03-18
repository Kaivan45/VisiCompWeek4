package com.codedotorg;

import java.util.Random;

public class GameLogic {

    private boolean gameOver;
    private Random rand;
    
    // Statistik & Skor
    private int userScore = 0;
    private int computerScore = 0;
    private int totalRounds = 0;
    private int winTarget = 3;

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

        // 1. Jangan hitung ronde jika tangan tidak terdeteksi (Neutral)
        if (user.contains("neutral")) {
            return "Posisikan tanganmu dengan jelas!";
        }

        // 2. Tambah jumlah ronde HANYA jika gerakan valid (Rock/Paper/Scissors)
        totalRounds++; 
        
        String roundResult = "";

        // Logika Pemenang Ronde
        if (user.contains(computer) || computer.contains(user)) {
            roundResult = "SERI!";
        } else if ((user.contains("rock") && computer.equals("scissors")) ||
                (user.contains("paper") && computer.equals("rock")) ||
                (user.contains("scissors") && computer.equals("paper"))) {
            userScore++;
            roundResult = "KAMU MENANG RONDE INI!";
        } else {
            computerScore++;
            roundResult = "KOMPUTER MENANG RONDE INI!";
        }

        // 3. Logika "Best of Five" (Siapa yang dapat 3 poin duluan menang)
        // Jika skor 3-1, maka total ronde yang dimainkan memang bisa 4.
        // Tapi jika kamu ingin MAIN FULL 5 RONDE, ubah kondisinya:
        if (totalRounds >= 5) {
            this.gameOver = true;
        }

        return roundResult + "\nSkor: User " + userScore + " - " + computerScore + " Comp";
    }

    // Fungsi untuk mengambil teks statistik akhir
    public String getFinalStats() {
        String winner = (userScore > computerScore) ? "SELAMAT! KAMU JUARANYA!" : "YAH! KOMPUTER JUARANYA!";
        return winner + "\n\n--- STATISTIK ---\n" +
               "Total Ronde: " + totalRounds + "\n" +
               "Skor Akhir: " + userScore + " - " + computerScore;
    }

    public boolean isGameOver() { return gameOver; }
    
    public void resetLogic() {
        this.gameOver = false;
        this.userScore = 0;
        this.computerScore = 0;
        this.totalRounds = 0;
    }
}
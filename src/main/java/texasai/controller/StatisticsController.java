package texasai.controller;

import texasai.model.Player;

import java.util.List;

public class StatisticsController {
    private int player1winnings = 0;

    public void initializeStatistics() {
        player1winnings = 0;
        //System.out.println(player1winnings);
    }

    public void storeWinners(List<Player> winners) {        
        for (Player winner : winners) {
            if (winner.getNumber() == 1) {
            	player1winnings++;
            }
        }
    }

    public int getPlayer1Wins() {
        return player1winnings;
    }

}

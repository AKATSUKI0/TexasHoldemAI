package texasai.controller;

import texasai.controller.opponentmodeling.OpponentModeler;
import texasai.model.*;
import texasai.model.cards.Card;
import texasai.model.gameproperties.GameProperties;
import texasai.utils.Logger;
import java.io.BufferedWriter; 
import java.io.File; 
import java.io.FileNotFoundException; 
import java.io.FileWriter; 
import java.io.IOException; 
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class GameHandController {
    protected final Logger logger;
    private final HandPowerRanker handPowerRanker;
    private final GameProperties gameProperties;
    private final StatisticsController statisticsController;
    private final HandStrengthEvaluator handStrengthEvaluator;
    private final OpponentModeler opponentModeler;

    @Inject
    public GameHandController(final Logger logger,
                              final HandPowerRanker handPowerRanker,
                              final GameProperties gameProperties,
                              final StatisticsController statisticsController,
                              final HandStrengthEvaluator handStrengthEvaluator,
                              final OpponentModeler opponentModeler) {
        this.logger = logger;
        this.handPowerRanker = handPowerRanker;
        this.gameProperties = gameProperties;
        this.statisticsController = statisticsController;
        this.handStrengthEvaluator = handStrengthEvaluator;
        this.opponentModeler = opponentModeler;
    }

    public void play(Game game) {
        
    	//System.out.println(game.getPlayers());
    	Player[] q = new Player[game.getPlayers().size()];
    	int i = 0; 
    	for (Player p: game.getPlayers()){	
        	if (p.getMoney() <= 0){
        		q[i] = p;
        		i++;
        	}
        }
    	for (int j = 0;j < i;j++){
    		game.kickPlayer(q[j]);
    	}

    	/**
    	//for draw picture
        for (Player player : game.getPlayers()) {			
    		try {
    			File csv = new File("/Users/AKATSUKI/Desktop/writers.csv"); // CSV数据文件 
  		      	BufferedWriter bw = new BufferedWriter(new FileWriter(csv, true)); 
  		      	bw.write(player.getMoney()+","+player.number);
  		      	//bw.write(",");
  		      	//bw.write(player.number);
  		      	bw.newLine(); 		      
  		      	bw.close(); 
  		      	bw.flush();
		    } catch (FileNotFoundException e) { 
		      // File对象的创建过程中的异常捕获 
		      e.printStackTrace(); 
		    } catch (IOException e) { 
		      // BufferedWriter在关闭对象捕捉异常 
		      e.printStackTrace(); 
		    } 	 
    	}
    	 **/
        
    	if(game.getPlayers().size() == 1){
        	logger.log("-----------------------------------------");
        	System.out.println("There is only one player left! Game Over!");
            logger.log("-----------------------------------------");
            logger.log("Statistics");
            logger.log("-----------------------------------------");
            logger.log("Number of hands played: " + game.gameHandsCount());
            for (Player player : game.getPlayers()) {
                logger.log("Winner is player " + player.number + " (" + player.getPlayerController().toString() + ")" + ": " + player
                        .getMoney() + "$");
            }
        	System.exit(0);
        }else{
        	logger.log("-----------------------------------------");
            logger.log("Statistics");
            logger.log("-----------------------------------------");
            logger.log("Number of hands played: " + game.gameHandsCount());
            for (Player player : game.getPlayers()) {
                logger.log("Player" + player.number + " (" + player.getPlayerController().toString() + ")" + ": " + player
                        .getMoney() + "$");
            }
    	}
        logger.log("-----------------------------------------");
        logger.logImportant("Game Hand #" + (game.gameHandsCount() + 1));
        logger.log("-----------------------------------------");
          
        GameHand gameHand = createGameHand(game);


        Boolean haveWinner = false;
        while (!gameHand.getBettingRoundName().equals(
                BettingRoundName.POST_RIVER)
                && !haveWinner) {
            haveWinner = playRound(gameHand);
        }

        if (!haveWinner) {
            showDown(gameHand);
        }
    }

    private GameHand createGameHand(Game game) {
        GameHand gameHand = new GameHand(game.getPlayers());
        game.addGameHand(gameHand);
        return gameHand;
    }

    protected Boolean playRound(GameHand gameHand) {
        gameHand.nextRound();
        logBettingRound(gameHand);
        int toPlay = gameHand.getPlayersCount();
        if (gameHand.getBettingRoundName().equals(BettingRoundName.PRE_FLOP)) {
            takeBlinds(gameHand);
            toPlay--; // Big blinds don't have to call on himself if no raise :)
        }

        while (toPlay > 0) {
            Player player = gameHand.getNextPlayer();
            BettingDecision bettingDecision = player.decide(gameHand);
            //System.out.println(player.number);
            //System.out.println(player.getMoney());
            //We can't raise at second turn
            //if (turn > numberOfPlayersAtBeginningOfRound
            //  && bettingDecision.equals(BettingDecision.RAISE)) {
            //	bettingDecision = BettingDecision.CALL;
            //}

            // After a raise, every active players after the raiser must play
            if (bettingDecision.equals(BettingDecision.RAISE)) {
            	// toPlay = gameHand.getPlayersCount() - 1;
            	// make sure the one wants to raise has enough money
            	if(player.getMoney() < gameHand.getCurrentBettingRound().getHighestBet() + gameProperties.getBigBlind()){
            		if(player.getMoney() >= gameHand.getCurrentBettingRound().getHighestBet()) {
            			bettingDecision = BettingDecision.CALL;
            		}
            		else{
            			bettingDecision = BettingDecision.FOLD;
            		}
            		
            	}
            	else{
            		toPlay = gameHand.getPlayersCount();
            
            	}
            }
            
            if(bettingDecision.equals(BettingDecision.CALL) && player.getMoney() < gameHand.getCurrentBettingRound().getHighestBet())
            	bettingDecision = BettingDecision.FOLD;
            
            applyDecision(gameHand, player, bettingDecision);
            toPlay--;
        }

        // Check if we have a winner
        if (gameHand.getPlayersCount() == 1) {
            Player winner = gameHand.getCurrentPlayer();
            winner.addMoney(gameHand.getTotalBets());
            logger.log("WINNER:" + winner + ": WIN! +" + gameHand.getTotalBets() + "$");

            return true;
        }
        return false;
    }

    private void logBettingRound(GameHand gameHand) {
        String logMsg = "---" + gameHand.getBettingRoundName();
        logMsg += " (" + gameHand.getPlayersCount() + " players, ";
        logMsg += gameHand.getTotalBets() + "$)";
        if (!gameHand.getSharedCards().isEmpty()) {
            logMsg += " " + gameHand.getSharedCards();
        }
        logger.log(logMsg);
    }

    private void takeBlinds(GameHand gameHand) {
        Player smallBlindPlayer = gameHand.getNextPlayer(); 
        Player bigBlindPlayer = gameHand.getNextPlayer();
        bigBlindPlayer.bigBlind = true;
        int sb = Math.min(smallBlindPlayer.getMoney(), gameProperties.getSmallBlind());
        int bb = Math.min(bigBlindPlayer.getMoney(), gameProperties.getBigBlind());
        
        logger.log(smallBlindPlayer + ": Small blind "
                + sb + "$");
        logger.log(bigBlindPlayer + ": Big blind "
                + bb + "$");

        gameHand.getCurrentBettingRound().placeBet(smallBlindPlayer,
                sb);
        gameHand.getCurrentBettingRound().placeBet(bigBlindPlayer,
        		bb);
    }

    private void applyDecision(GameHand gameHand, Player player, BettingDecision bettingDecision) {
        double handStrength = handStrengthEvaluator.evaluate(player.getHoleCards(), gameHand.getSharedCards(),
                gameHand.getPlayersCount());
        gameHand.applyDecision(player, bettingDecision, gameProperties, handStrength);

        BettingRound bettingRound = gameHand.getCurrentBettingRound();
        logger.log(player + ": " + bettingDecision + " "
                + bettingRound.getBetForPlayer(player) + "$");
    }

    private List<Player> getWinners(GameHand gameHand) {
        Iterable<Player> activePlayers = gameHand.getPlayers();
        List<Card> sharedCards = gameHand.getSharedCards();

        HandPower bestHandPower = null;
        List<Player> winners = new ArrayList<Player>();
        for (Player player : activePlayers) {
            List<Card> mergeCards = new ArrayList<Card>(player.getHoleCards());
            mergeCards.addAll(sharedCards);
            HandPower handPower = handPowerRanker.rank(mergeCards);

            logger.log(player + ": " + handPower);

            if (bestHandPower == null || handPower.compareTo(bestHandPower) > 0) {
                winners.clear();
                winners.add(player);
                bestHandPower = handPower;
            } else if (handPower.equals(bestHandPower)) {
                winners.add(player);
            }
        }
        statisticsController.storeWinners(winners);
        return winners;
    }

    protected void showDown(GameHand gameHand) {
        logger.log("--- Showdown");

        // Showdown
        List<Player> winners = getWinners(gameHand);

        // Gains
        int gain = gameHand.getTotalBets() / winners.size();
        int modulo = gameHand.getTotalBets() % winners.size();
        for (Player winner : winners) {
            int gainAndModulo = gain;
            if (modulo > 0) {
                gainAndModulo += modulo;
            }
            winner.addMoney(gainAndModulo);
            logger.log("WINNER: "+winner + ": WIN! +" + gainAndModulo + "$");

            modulo--;
        }

        // Opponent modeling
        opponentModeler.save(gameHand);
        

    }
}

package texasai.controller.preflopsim;

import com.google.inject.Inject;

import texasai.controller.GameHandController;
import texasai.controller.HandPowerRanker;
import texasai.controller.HandStrengthEvaluator;
import texasai.controller.StatisticsController;
import texasai.controller.opponentmodeling.OpponentModeler;
import texasai.model.BettingRoundName;
import texasai.model.Game;
import texasai.model.GameHand;
import texasai.model.cards.EquivalenceClass;
import texasai.model.preflopsim.GameHandPreFlopRoll;
import texasai.model.gameproperties.GameProperties;
import texasai.utils.Logger;

public class GameHandControllerPreFlopRoll extends GameHandController {

    @Inject
    public GameHandControllerPreFlopRoll(Logger logger,
            HandPowerRanker handPowerRanker, GameProperties gameProperties,
            StatisticsController statisticsController, HandStrengthEvaluator handStrengthEvaluator, OpponentModeler opponentModeler) {
        super(logger, handPowerRanker, gameProperties, statisticsController, handStrengthEvaluator, opponentModeler);
    }

    public void play(Game game, EquivalenceClass equivalenceClass) {
        logger.log("-----------------------------------------");
        logger.log("Game Hand #" + (game.gameHandsCount() + 1));
        logger.log("-----------------------------------------");
        logger.log("-----------------------------------------");
        logger.log(equivalenceClass.toString());
        logger.log("-----------------------------------------");
        GameHand gameHand = createGameHand(game, equivalenceClass);
        //System.out.println(equivalenceClass);

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

    private GameHand createGameHand(Game game, EquivalenceClass equivalenceClass) {
        GameHand gameHand = new GameHandPreFlopRoll(game,game.getPlayers(),
                equivalenceClass);
        game.addGameHand(gameHand);
        return gameHand;
    }

}

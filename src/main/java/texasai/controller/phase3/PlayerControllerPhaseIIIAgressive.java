package texasai.controller.phase3;

import java.util.Random;

import texasai.controller.HandStrengthEvaluator;
import texasai.controller.phase2.PlayerControllerPhaseIIBluffConservative;
import texasai.controller.opponentmodeling.OpponentModeler;
import texasai.model.BettingDecision;
import texasai.model.GameHand;
import texasai.model.Player;

import javax.inject.Inject;

public class PlayerControllerPhaseIIIAgressive extends PlayerControllerPhaseIII {
    @Inject
    public PlayerControllerPhaseIIIAgressive(PlayerControllerPhaseIIBluffConservative playerControllerPhaseIIBluffConservative,
                                                HandStrengthEvaluator handStrengthEvaluator,
                                                OpponentModeler opponentModeler) {
        super(playerControllerPhaseIIBluffConservative, handStrengthEvaluator, opponentModeler);
    }

    @Override
    public String toString() {
        return "PhaseIII Agressive";
    }

    @Override
    protected BettingDecision decideBet(GameHand gameHand, Player player,
                                        int type,
                                        double x,
                                        int opponentsModeledCount) {
    	System.out.println(type);
        Random rand = new Random();  
        Double n = rand.nextDouble();
        if (type == 0 || (type == 1 && n > 0.5)) {       	
        	GameHand.raiseValue = (int) (0.5 * (1 + n) * player.getMoney());
        	return BettingDecision.RAISE;
        } else if (type == 3) {
        	GameHand.raiseValue = (int) (Math.exp(x-1) * 0.5 * player.getMoney());
        	return BettingDecision.RAISE;
        } else if (type == 2){
        	return BettingDecision.CALL;
        } else{
        	return BettingDecision.FOLD;
        }
    }
}

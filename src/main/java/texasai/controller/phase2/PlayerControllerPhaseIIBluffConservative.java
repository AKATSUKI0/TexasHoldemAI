package texasai.controller.phase2;

import texasai.controller.EquivalenceClassController;
import texasai.controller.HandPotentialEvaluator;
import texasai.controller.HandPotentialEvaluator1;
import texasai.controller.HandStrengthEvaluator;
import texasai.model.BettingDecision;
import texasai.model.BettingRoundName;
import texasai.model.GameHand;
import texasai.model.Player;
import texasai.model.cards.Card;
import texasai.model.cards.EquivalenceClass;
import texasai.model.opponentmodeling.ContextPlayers;
import texasai.persistence.PreFlopPersistence;

import javax.inject.Inject;

import java.util.List;
import java.util.Random;

public class PlayerControllerPhaseIIBluffConservative extends PlayerControllerPhaseII {
    private final EquivalenceClassController equivalenceClassController;
    private final PreFlopPersistence preFlopPersistence;

    @Inject
    public PlayerControllerPhaseIIBluffConservative(final EquivalenceClassController equivalenceClassController,
                                        final PreFlopPersistence preFlopPersistence,
                                        final HandStrengthEvaluator handStrengthEvaluator,
                                        final HandPotentialEvaluator handPotentialEvaluator,
                                        final HandPotentialEvaluator1 handPotentialEvaluator1) {
        super(handStrengthEvaluator,handPotentialEvaluator,handPotentialEvaluator1);

        this.equivalenceClassController = equivalenceClassController;
        this.preFlopPersistence = preFlopPersistence;
    }

    @Override
    public String toString() {
        return "PhaseII bluffConservative";
    }

    @Override
    public BettingDecision decidePreFlop(Player player, GameHand gameHand, List<Card> cards) {
        Card card1 = cards.get(0);
        Card card2 = cards.get(1);
        EquivalenceClass equivalenceClass = this.equivalenceClassController.cards2Equivalence(card1, card2);
        double percentageOfWins = preFlopPersistence.retrieve(gameHand.getPlayers().size(), equivalenceClass);
        //System.out.println(percentageOfWins);
        //System.out.println(gameHand.getCurrentBettingRound().getBetForPlayer(player));
        int bigB_position = 0;
        int my_position = 0;
        int idx = 0; 
        for (Player p : gameHand.getPlayers()) {
        	
        	if(p.equals(player)){
        		my_position = idx;
        	}
        	if(p.bigBlind){
        		bigB_position = idx;
        	}
        	idx++;
        }
        int postion = bigB_position > my_position ? gameHand.getPlayersCount() - bigB_position + my_position 
        		: my_position - bigB_position;
        
        int c = gameHand.getPlayersCount()*2; // the constant
        double threshold = percentageOfWins * (double)(1.0 + postion / c);		
   
        if (threshold > 0.5) {
        	double x = (threshold - 0.5)/0.6;
        	GameHand.raiseValue = (int) (Math.exp(x-1) * player.getMoney());
        	return BettingDecision.RAISE;
        }else if (threshold > 0.25){
            return BettingDecision.CALL;
        }
        return BettingDecision.FOLD;
    }

    @Override
    public BettingDecision decideAfterFlop(Player player, GameHand gameHand, List<Card> cards) {
        double p = calculateCoefficient(gameHand, player);
        double q = calculatehandpotential(gameHand,player);
        Random rand = new Random();  
        Double n = rand.nextDouble();
        // Bluff
        if(p < 0.2 && n > 0.8){
            if(gameHand.getBettingRoundName().equals(BettingRoundName.POST_FLOP)){
            	GameHand.raiseValue = (int) (Math.exp(n-1) * 0.5 * player.getMoney());
            	return BettingDecision.RAISE;
            }else if(ContextPlayers.valueFor(gameHand.getPlayersCount()).equals(ContextPlayers.FEW)){
                // Not too much player in post-turn and post-river
            	GameHand.raiseValue = (int) (Math.exp(n-1) * 0.5 * player.getMoney()); 
            	return BettingDecision.RAISE;
            }else if(gameHand.getPlayers().getFirst().equals(player)){
            	return BettingDecision.CALL;
            }else{
                return BettingDecision.FOLD;
            }
        }
        if(p < 0.3){
        	double potodd = 20/(gameHand.getTotalBets()+20);
        	
            if(gameHand.getBettingRoundName().equals(BettingRoundName.POST_FLOP) && potodd < q){
                return BettingDecision.CALL;
            }
            if(gameHand.getBettingRoundName().equals(BettingRoundName.POST_TURN) && potodd < q){
                return BettingDecision.CALL;
            }
        }

        if (p > 0.8) {
        	double x = (p - 0.8)/0.4;
        	GameHand.raiseValue = (int) (Math.exp(x-1) * player.getMoney());
            return BettingDecision.RAISE;
        } else if (p > 0.4 || gameHand.getPlayers().getFirst().equals(player)) {
            return BettingDecision.CALL;
        }
        return BettingDecision.FOLD;
    }
}

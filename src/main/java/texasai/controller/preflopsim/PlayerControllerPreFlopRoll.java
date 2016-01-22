package texasai.controller.preflopsim;

//import texasai.controller.HandPowerRanker;
import texasai.controller.PlayerController;
import texasai.model.*;
import texasai.model.cards.Card;

import javax.inject.Inject;
import java.util.List;

/**
 * A naive player that cannot fold but only bet. Used during pre flop rollout
 * simulations
 */
public class PlayerControllerPreFlopRoll extends PlayerController {
    //private final HandPowerRanker handPowerRanker;

    @Inject
    //public PlayerControllerPreFlopRoll(final HandPowerRanker handPowerRanker) {
    //    this.handPowerRanker = handPowerRanker;
    //}

    @Override
    public String toString() {
        return "Preflop";
    }

    @Override
    public BettingDecision decidePreFlop(Player player, GameHand gameHand,
                                         List<Card> cards) {
    	return BettingDecision.CALL;
    }

    @Override
    public BettingDecision decideAfterFlop(Player player, GameHand gameHand, List<Card> cards) {
        return BettingDecision.CALL;
    }
}

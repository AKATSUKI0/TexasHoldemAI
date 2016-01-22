package texasai.controller.phase1;

import texasai.controller.HandPowerRanker;
import texasai.controller.PlayerController;
import texasai.model.*;
import texasai.model.cards.Card;

import javax.inject.Inject;

import java.util.List;

public class PlayerControllerPhaseINormal extends PlayerController {
    private final HandPowerRanker handPowerRanker;

    @Inject
    public PlayerControllerPhaseINormal(final HandPowerRanker handPowerRanker) {
        this.handPowerRanker = handPowerRanker;
    }

    @Override
    public String toString() {
        return "PhaseI normal";
    }

    @Override
    public BettingDecision decidePreFlop(Player player, GameHand gameHand,
                                         List<Card> cards) {
        Card card1 = cards.get(0);
        Card card2 = cards.get(1);

        if (card1.getNumber().equals(card2.getNumber())) {
        	GameHand.raiseValue = 40;
            return BettingDecision.RAISE;
        } else if (card1.getNumber().getPower() + card2.getNumber().getPower() > 16) {
            return BettingDecision.CALL;
        } else {
            return BettingDecision.FOLD;
        }
    }

    @Override
    public BettingDecision decideAfterFlop(Player player, GameHand gameHand, List<Card> cards) {
        HandPower handPower = handPowerRanker.rank(cards);

        HandPowerType handPowerType = handPower.getHandPowerType();
        if (handPowerType.equals(HandPowerType.HIGH_CARD) && !gameHand.getPlayers().getFirst().equals(player)) {
            return BettingDecision.FOLD;
        } else if (handPowerType.getPower() >= HandPowerType.STRAIGHT.getPower()) {
        	GameHand.raiseValue = 40;
            return BettingDecision.RAISE;
        } else {
            return BettingDecision.CALL;
        }
    }
}

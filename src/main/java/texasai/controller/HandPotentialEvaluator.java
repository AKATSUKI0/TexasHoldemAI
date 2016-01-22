package texasai.controller;

import texasai.model.BettingRoundName;
import texasai.model.HandPower;
import texasai.model.cards.Card;
import texasai.model.cards.Deck;
import texasai.model.GameHand;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;

public class HandPotentialEvaluator {

    private final HandPowerRanker handPowerRanker;
    
    @Inject
    public HandPotentialEvaluator(final HandPowerRanker handPowerRanker) {
        this.handPowerRanker = handPowerRanker;
    }

    public double evaluate(List<Card> playerHoleCards, List<Card> sharedCards, Integer numberOfPlayers, final GameHand gameHand) {
        if(sharedCards == null || sharedCards.isEmpty() || gameHand.getBettingRoundName().equals(BettingRoundName.PRE_FLOP) 
        		|| gameHand.getBettingRoundName().equals(BettingRoundName.POST_RIVER) || gameHand.getBettingRoundName().equals(BettingRoundName.POST_TURN)){
            return 0d;
        }
        int [][] HP = new int[3][3];
        
        Deck deck = new Deck();
        Card hole1 = playerHoleCards.get(0);
        Card hole2 = playerHoleCards.get(1);
        deck.removeCard(hole1);
        deck.removeCard(hole2);
        for (Card card : sharedCards) {
                deck.removeCard(card);
        }
        
        List<List<Card>> couplesOfCards = deck.fromDeckToCouplesOfCard();

        List<Card> playerCards = new ArrayList<Card>();
        playerCards.addAll(playerHoleCards);
        playerCards.addAll(sharedCards);
        HandPower playerRank = handPowerRanker.rank(playerCards);

        for (List<Card> couple : couplesOfCards) {
            List<Card> opponentCards = new ArrayList<Card>();
            opponentCards.addAll(couple);
            opponentCards.addAll(sharedCards);
            HandPower opponentRank = handPowerRanker.rank(opponentCards);
            int index = 0;
            
            int result = playerRank.compareTo(opponentRank);
            if (result > 0) {
                index = 0;
            } else if (result < 0) {
            	index = 2;
            } else {
            	index = 1;
            }
            //System.out.println(Total[0]);
            for (List<Card> add : couplesOfCards){
            	if (!add.get(0).equals(couple.get(0)) && !add.get(1).equals(couple.get(1))
            			&& !add.get(1).equals(couple.get(0)) && !add.get(0).equals(couple.get(1))){
                    List<Card> playerCards7 = new ArrayList<Card>();
                    playerCards7.addAll(playerCards);
                    playerCards7.addAll(add);
                    List<Card> opponentCards7 = new ArrayList<Card>();
                    opponentCards7.addAll(opponentCards);
            		opponentCards7.addAll(add);
            		HandPower playerRank7 = handPowerRanker.rank(playerCards7);
            		HandPower opponentRank7 = handPowerRanker.rank(opponentCards7);
            		result = playerRank7.compareTo(opponentRank7);
            		if (result > 0) {
            			HP[index][0]++;
            		} else if (result < 0) {
            			HP[index][2]++;
            		} else {
            			HP[index][1]++;
            		}
            	}
            }
            
        }
        double pp = HP[2][0] + HP[2][1]/2 + HP[1][0]/2;
        double ptotal = (HP[2][1]+HP[2][0]+HP[2][2]) + (HP[1][0]+HP[1][1]+HP[1][2])/2;
        //System.out.println(pp/ptotal);
        return Math.pow(pp/ptotal, numberOfPlayers);
        
    }


}

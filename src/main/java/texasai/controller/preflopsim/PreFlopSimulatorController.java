package texasai.controller.preflopsim;

import texasai.controller.EquivalenceClassController;
import texasai.controller.StatisticsController;
import texasai.model.Game;
import texasai.model.Player;
import texasai.model.cards.EquivalenceClass;
import texasai.persistence.PreFlopPersistence;
import texasai.model.gameproperties.GameProperties;
import texasai.utils.Logger;

import javax.inject.Inject;
import java.util.Collection;

public class PreFlopSimulatorController {
    private static final int ROLLOUTS_PER_EQUIV_CLASS = 1000;

    private final Game game = new Game();
    private final Logger logger;
    private final GameProperties gameProperties;
    private final PlayerControllerPreFlopRoll playerControllerPreFlopRoll;
    private final EquivalenceClassController equivalenceClassController;
    private final GameHandControllerPreFlopRoll gameHandControllerPreFlopRoll;
    private final StatisticsController statisticsController;
    private final PreFlopPersistence preFlopPersistence;

    @Inject
    public PreFlopSimulatorController(final Logger logger, final GameProperties gameProperties,
                                      final PlayerControllerPreFlopRoll playerControllerPreFlopRoll,
                                      final EquivalenceClassController equivalenceClassController,
                                      final GameHandControllerPreFlopRoll gameHandControllerPreFlopRoll,
                                      final StatisticsController statisticsController,
                                      final PreFlopPersistence preFlopPersistence) {
        this.logger = logger;
        this.gameProperties = gameProperties;
        this.playerControllerPreFlopRoll = playerControllerPreFlopRoll;
        this.equivalenceClassController = equivalenceClassController;
        this.gameHandControllerPreFlopRoll = gameHandControllerPreFlopRoll;
        this.statisticsController = statisticsController;
        this.preFlopPersistence = preFlopPersistence;
    }

    public void play() {
        this.equivalenceClassController.generateAllEquivalenceClass();

        game.addPlayer(new Player(1, 0, playerControllerPreFlopRoll));
        
        //System.out.println(gameProperties.getInitialMoney());
        Collection<EquivalenceClass> equivalenceClasses = equivalenceClassController.getEquivalenceClasses();

        for (int numberOfPlayers = 2; numberOfPlayers <= 9; numberOfPlayers++) {
            game.addPlayer(new Player(numberOfPlayers, 0, playerControllerPreFlopRoll));

            for (EquivalenceClass equivalenceClass : equivalenceClasses) {
                statisticsController.initializeStatistics();
                //System.out.println(equivalenceClass);
                for (int i = 0; i < ROLLOUTS_PER_EQUIV_CLASS; i++) {
                	for (Player p : game.getPlayers()){
                		p.money = 1000;
                	}
                    gameHandControllerPreFlopRoll.play(game, equivalenceClass);
                    game.setNextDealer();
                }

                double percentageWin = (double) statisticsController.getPlayer1Wins() / ROLLOUTS_PER_EQUIV_CLASS;
                preFlopPersistence.persist(numberOfPlayers, equivalenceClass, percentageWin);

                logger.logImportant("=================");
                logger.logImportant("STATISTICS FOR EQUIVALENCE CLASS "
                        + equivalenceClass.toString());
                logger.logImportant("Number of hands played: " + ROLLOUTS_PER_EQUIV_CLASS);
                logger.logImportant("Number players: " + numberOfPlayers);
                logger.logImportant("Percentage of wins is " + percentageWin);
            }
        }
    }
}

package texasai.controller;

import com.google.inject.AbstractModule;
import texasai.controller.opponentmodeling.OpponentModeler;
import texasai.controller.phase1.PlayerControllerPhaseIBluff;
import texasai.controller.phase1.PlayerControllerPhaseINormal;
import texasai.controller.phase2.PlayerControllerPhaseIIBluff;
import texasai.controller.phase2.PlayerControllerPhaseIINormal;
import texasai.controller.phase3.PlayerControllerPhaseIIIAgressive;
import texasai.controller.phase3.PlayerControllerPhaseIIIConservative;
import texasai.controller.preflopsim.PreFlopSimulatorModule;

import javax.inject.Singleton;

public class ControllerModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new PreFlopSimulatorModule());

        bind(PokerController.class).in(Singleton.class);
        bind(GameHandController.class).in(Singleton.class);
        bind(HandPowerRanker.class).in(Singleton.class);
        bind(StatisticsController.class).in(Singleton.class);
        bind(HandStrengthEvaluator.class).in(Singleton.class);
        bind(EquivalenceClassController.class).in(Singleton.class);
        bind(OpponentModeler.class).in(Singleton.class);

        bind(PlayerControllerPhaseINormal.class).in(Singleton.class);
        bind(PlayerControllerPhaseIBluff.class).in(Singleton.class);
        bind(PlayerControllerPhaseIINormal.class).in(Singleton.class);
        bind(PlayerControllerPhaseIIBluff.class).in(Singleton.class);
        bind(PlayerControllerPhaseIIIAgressive.class).in(Singleton.class);
        bind(PlayerControllerPhaseIIIConservative.class).in(Singleton.class);
    }
}

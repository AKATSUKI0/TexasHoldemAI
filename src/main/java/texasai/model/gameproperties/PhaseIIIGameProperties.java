package texasai.model.gameproperties;

import texasai.controller.phase2.PlayerControllerPhaseIIBluff;
import texasai.controller.phase2.PlayerControllerPhaseIINormal;
import texasai.controller.phase3.PlayerControllerPhaseIIIAgressive;
import texasai.controller.phase3.PlayerControllerPhaseIIIConservative;
import texasai.model.Player;

import javax.inject.Inject;

public class PhaseIIIGameProperties extends GameProperties {
    @Inject
    public PhaseIIIGameProperties(final PlayerControllerPhaseIINormal playerControllerPhaseIINormal,
                                  final PlayerControllerPhaseIIBluff playerControllerPhaseIIBluff,
                                  final PlayerControllerPhaseIIIAgressive playerControllerPhaseIIIAgressive,
                                  final PlayerControllerPhaseIIIConservative playerControllerPhaseIIIConservative) {
        super(1000, 1000, 20, 10);

        addPlayer(new Player(1, getInitialMoney(), playerControllerPhaseIIBluff));
        addPlayer(new Player(2, getInitialMoney(), playerControllerPhaseIINormal));
        addPlayer(new Player(3, getInitialMoney(), playerControllerPhaseIIIAgressive));
        addPlayer(new Player(4, getInitialMoney(), playerControllerPhaseIIIConservative));
    }
}

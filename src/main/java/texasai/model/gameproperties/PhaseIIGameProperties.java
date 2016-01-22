package texasai.model.gameproperties;

import texasai.controller.phase2.PlayerControllerPhaseIIBluff;
import texasai.controller.phase2.PlayerControllerPhaseIINormal;
import texasai.model.Player;

import javax.inject.Inject;

public class PhaseIIGameProperties extends GameProperties {
    @Inject
    public PhaseIIGameProperties(final PlayerControllerPhaseIINormal playerControllerPhaseIINormal,
                                 final PlayerControllerPhaseIIBluff playerControllerPhaseIIBluff) {
        super(1000, 1000, 20, 10);

        addPlayer(new Player(1, getInitialMoney(), playerControllerPhaseIIBluff));
        addPlayer(new Player(2, getInitialMoney(), playerControllerPhaseIIBluff));
        addPlayer(new Player(3, getInitialMoney(), playerControllerPhaseIINormal));
        addPlayer(new Player(4, getInitialMoney(), playerControllerPhaseIINormal));
    }
}

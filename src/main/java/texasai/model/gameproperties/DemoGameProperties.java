package texasai.model.gameproperties;

import texasai.controller.phase1.PlayerControllerPhaseINormal;
import texasai.controller.phase2.PlayerControllerPhaseIIBluff;
import texasai.controller.phase2.PlayerControllerPhaseIIBluffConservative;
import texasai.controller.phase2.PlayerControllerPhaseIIBluffAggressive;
import texasai.controller.phase2.PlayerControllerPhaseIINormal;
import texasai.controller.phase3.PlayerControllerPhaseIIIAgressive;
import texasai.controller.phase3.PlayerControllerPhaseIIIConservative;
import texasai.model.Player;

import javax.inject.Inject;
	
public class DemoGameProperties extends GameProperties {
    @Inject
    public DemoGameProperties(final PlayerControllerPhaseINormal playerControllerPhaseINormal,
    						  final PlayerControllerPhaseIINormal playerControllerPhaseIINormal,
                              final PlayerControllerPhaseIIBluff playerControllerPhaseIIBluff,
                              final PlayerControllerPhaseIIBluffConservative playerControllerPhaseIIBluffConservative,
                              final PlayerControllerPhaseIIBluffAggressive playerControllerPhaseIIBluffAggressive,
                              final PlayerControllerPhaseIIIAgressive playerControllerPhaseIIIAgressive,
                              final PlayerControllerPhaseIIIConservative playerControllerPhaseIIIConservative) {
        super(100, 10000, 20, 10);
        
        addPlayer(new Player(1, getInitialMoney(), playerControllerPhaseIIBluffConservative));
        //addPlayer(new Player(2, getInitialMoney(), playerControllerPhaseIIBluff));
        //addPlayer(new Player(3, getInitialMoney(), playerControllerPhaseIIBluffAggressive));
        addPlayer(new Player(4, getInitialMoney(), playerControllerPhaseIINormal));
        addPlayer(new Player(5, getInitialMoney(), playerControllerPhaseIIIAgressive));
        addPlayer(new Player(6, getInitialMoney(), playerControllerPhaseIIIConservative));
        //addPlayer(new Player(7, getInitialMoney(), playerControllerPhaseINormal));
    }
}

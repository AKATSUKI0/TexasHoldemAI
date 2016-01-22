package texasai;

import com.google.inject.Guice;
import com.google.inject.Injector;
import texasai.controller.preflopsim.PreFlopSimulatorController;
import texasai.dependencyinjection.GamePropertiesParameter;
import texasai.dependencyinjection.LogLevel;
import texasai.dependencyinjection.TexasModule;

public class RunPreFlopSimulator {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new TexasModule(LogLevel.IMPORTANT, GamePropertiesParameter.PHASE1));

        PreFlopSimulatorController preFlopSimulatorController = injector
                .getInstance(PreFlopSimulatorController.class);
        preFlopSimulatorController.play();
    }
}

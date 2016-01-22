package texasai;

import com.google.inject.Guice;
import com.google.inject.Injector;
import texasai.controller.PokerController;
import texasai.dependencyinjection.GamePropertiesParameter;
import texasai.dependencyinjection.LogLevel;
import texasai.dependencyinjection.TexasModule;

public class Play {
    public static void main(String[] args) {
        String gameP = "demo";
        if(args.length == 1){
            gameP = args[0];
        }

        Injector injector = Guice.createInjector(new TexasModule(LogLevel.ALL, GamePropertiesParameter.fromString(gameP)));

        PokerController pokerController = injector.getInstance(PokerController.class);
        pokerController.play();
    }
}

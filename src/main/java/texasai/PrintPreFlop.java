package texasai;

import com.google.inject.Guice;
import com.google.inject.Injector;
import texasai.dependencyinjection.GamePropertiesParameter;
import texasai.dependencyinjection.LogLevel;
import texasai.dependencyinjection.TexasModule;
import texasai.persistence.PreFlopPersistence;

public class PrintPreFlop {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new TexasModule(LogLevel.ALL, GamePropertiesParameter.DEMO));

        PreFlopPersistence preFlopPersistence = injector.getInstance(PreFlopPersistence.class);
        preFlopPersistence.print();
    }
}

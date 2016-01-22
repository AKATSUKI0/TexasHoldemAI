package texasai;

import com.google.inject.Guice;
import com.google.inject.Injector;
import texasai.dependencyinjection.GamePropertiesParameter;
import texasai.dependencyinjection.LogLevel;
import texasai.dependencyinjection.TexasModule;
import texasai.persistence.OpponentsModelPersistence;

public class PrintOpponentsModel {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new TexasModule(LogLevel.ALL, GamePropertiesParameter.DEMO));

        OpponentsModelPersistence opponentsModelPersistence = injector.getInstance(OpponentsModelPersistence.class);
        opponentsModelPersistence.print();
    }
}

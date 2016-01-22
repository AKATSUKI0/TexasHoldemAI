package texasai.dependencyinjection;

import com.google.inject.AbstractModule;
import texasai.controller.ControllerModule;
import texasai.model.gameproperties.GameProperties;
import texasai.persistence.PersistenceModule;
import texasai.utils.Logger;

import javax.inject.Singleton;

public class TexasModule extends AbstractModule {
    private final LogLevel logLevel;
    private final GamePropertiesParameter gamePropertiesParameter;

    public TexasModule(LogLevel logLevel, GamePropertiesParameter gamePropertiesParameter) {
        this.logLevel = logLevel;
        this.gamePropertiesParameter = gamePropertiesParameter;
    }

    public TexasModule() {
        logLevel = LogLevel.ALL;
        gamePropertiesParameter = GamePropertiesParameter.DEMO;
    }

    @Override
    protected void configure() {
        install(new ControllerModule());
        install(new PersistenceModule());

        bind(LogLevel.class).toInstance(logLevel);
        bind(GamePropertiesParameter.class).toInstance(gamePropertiesParameter);

        bind(GameProperties.class).toProvider(GamePropertiesProvider.class).in(Singleton.class);
        bind(Logger.class).toProvider(LoggerProvider.class).in(Singleton.class);
    }
}

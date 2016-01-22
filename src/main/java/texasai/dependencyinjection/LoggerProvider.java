package texasai.dependencyinjection;

import texasai.utils.ConsoleLogger;
import texasai.utils.ImportantLogger;
import texasai.utils.Logger;

import javax.inject.Inject;
import javax.inject.Provider;

public class LoggerProvider implements Provider<Logger> {
    private final LogLevel logLevel;

    @Inject
    public LoggerProvider(final LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public Logger get() {
        switch (logLevel){
            case IMPORTANT:
                return new ImportantLogger();
            default:
                return new ConsoleLogger();
        }
    }
}

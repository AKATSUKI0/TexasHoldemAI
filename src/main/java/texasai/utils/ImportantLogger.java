package texasai.utils;

public class ImportantLogger implements Logger {
    public void log(String message) {
        // Don't print not important information
    }

    public void logImportant(String message) {
        System.out.println(message);
    }
}

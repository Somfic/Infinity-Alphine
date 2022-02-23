package logging;

public interface LogListener {
    void onLog(LogLevel level, Exception exception, String message);
}

package core;

import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionLogger {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExceptionLogger.class);

    public static <T> Function<Throwable, T> get() {
        return throwable -> {
            LOGGER.error("Uncaught exception", throwable);
            return null;
        };
    }

}

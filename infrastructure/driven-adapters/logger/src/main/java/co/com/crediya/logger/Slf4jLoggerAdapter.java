package co.com.crediya.logger;

import co.com.crediya.ports.CrediyaLoggerPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Slf4jLoggerAdapter implements CrediyaLoggerPort {

    private final Logger logger = LoggerFactory.getLogger(Slf4jLoggerAdapter.class);


    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void info(String message, Object... objects) {
        logger.info(message, objects);
    }

    @Override
    public void warn(String message) {
        logger.warn(message);
    }

    @Override
    public void warn(String message, Object... objects) {
        logger.warn(message, objects);
    }


}

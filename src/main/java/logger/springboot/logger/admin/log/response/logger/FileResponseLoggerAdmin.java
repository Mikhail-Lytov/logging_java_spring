package logger.springboot.logger.admin.log.response.logger;

import logger.springboot.logger.admin.log.dto.LogErrorEntity;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FileResponseLoggerAdmin implements ResponseLoggerAdmin {

    private static final Logger logger = LoggerFactory.getLogger(FileResponseLoggerAdmin.class);

    @Override
    public void logResponse(HttpServletResponse response, String body, Long startTime, Long endTime) {

        LogErrorEntity logErrorEntity = new LogErrorEntity();
        logErrorEntity.setStatus(response.getStatus());
        logErrorEntity.setBodyError(body != null ? body : "NO BODY");
        logErrorEntity.setTimeRequest(endTime - startTime);

        logger.error("status response: {}, body response: {}, time response: {}ms", logErrorEntity.getStatus(), logErrorEntity.getBodyError(), logErrorEntity.getTimeRequest());
    }
}

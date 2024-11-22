package logger.springboot.logger.admin.log.response.logger;

import jakarta.servlet.http.HttpServletResponse;

public interface ResponseLoggerAdmin {
    void logResponse(HttpServletResponse response, String body, Long startTime, Long endTime);
}

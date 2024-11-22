package logger.springboot.logger.admin.log.request.logger;

import jakarta.servlet.http.HttpServletRequest;

public interface RequestLoggerAdmin {
    void logRequest(String prefix, HttpServletRequest request, String body, Long startTime, Long endTime);
}

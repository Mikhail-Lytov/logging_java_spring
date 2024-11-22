package logger.springboot.logger.admin.log;

import logger.springboot.logger.admin.configuration.LoggerAdminMapProperties;
import logger.springboot.logger.admin.log.request.logger.RequestLoggerAdmin;
import logger.springboot.logger.admin.log.response.logger.ResponseLoggerAdmin;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;


@Component
public class RequestLoggingFilterAdminImpl extends OncePerRequestFilter implements RequestLoggingFilterAdmin {

    private final RequestLoggerAdmin requestLoggerAdmin;

    private final ResponseLoggerAdmin responseLogger;

    private LoggerAdminMapProperties properties;

    public RequestLoggingFilterAdminImpl(RequestLoggerAdmin requestLoggerAdmin, ResponseLoggerAdmin responseLogger, LoggerAdminMapProperties properties) {
        this.requestLoggerAdmin = requestLoggerAdmin;
        this.responseLogger = responseLogger;
        this.properties = properties;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        boolean completeRequest = false;
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrapperResponse = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();

        filterChain.doFilter(wrappedRequest, wrapperResponse.getResponse());

        long endTime = System.currentTimeMillis();

        for (String adminUrl : properties.getAuthorizationController()) {

            if (adminUrl.endsWith("/*")) {
                if (request.getRequestURI().startsWith(adminUrl.substring(0, adminUrl.length() - 2))) {
                    completeRequest = true;
                    requestLoggerAdmin.logRequest("User Management", request, mapBody(wrappedRequest), startTime, endTime);
                }
            }
            else {
                if (request.getRequestURI().equals(adminUrl)) {
                    completeRequest = true;
                    requestLoggerAdmin.logRequest("User Management", request, mapBody(wrappedRequest), startTime, endTime);
                }
            }

        }

        if (!completeRequest) {
            requestLoggerAdmin.logRequest("User Action", request, mapBody(wrappedRequest), startTime, endTime);
        }
        if (response.getStatus() != 200) {
            responseLogger.logResponse(wrapperResponse, mapResponseBody(wrapperResponse), startTime, endTime);
        }
    }

    private String mapResponseBody(ContentCachingResponseWrapper response) {
        StringBuilder body = new StringBuilder(
                new String(response.getContentAsByteArray(), StandardCharsets.UTF_8).replaceAll("\\s+", "")
        );
        return body.toString();
    }

    private String mapBody(ContentCachingRequestWrapper wrappedRequest) {
        StringBuilder body = new StringBuilder(
                new String(wrappedRequest.getContentAsByteArray(), StandardCharsets.UTF_8).replaceAll("\\s+", "")
        );
        Map<String, String[]> parameter = wrappedRequest.getParameterMap();
        for (String key : parameter.keySet()) {
            body
                    .append(" Parameter: ")
                    .append("\"")
                    .append(key)
                    .append("\"")
                    .append(", Parameter value: ");
            for (String value : parameter.get(key)) {
                body.append("\"")
                        .append(value)
                        .append("\"");
            }
        }
        return body.toString();
    }
}
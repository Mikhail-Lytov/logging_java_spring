package logger.springboot.logger.admin.configuration;

import logger.springboot.logger.admin.log.RequestLoggingFilterAdmin;
import logger.springboot.logger.admin.log.RequestLoggingFilterAdminImpl;
import logger.springboot.logger.admin.log.request.logger.FileRequestLoggerAdmin;
import logger.springboot.logger.admin.log.request.logger.RequestLoggerAdmin;
import logger.springboot.logger.admin.log.response.logger.FileResponseLoggerAdmin;
import logger.springboot.logger.admin.log.response.logger.ResponseLoggerAdmin;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LoggerAdminMapProperties.class)
public class LoggerAdminConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public RequestLoggerAdmin requestLogger() {
        return new FileRequestLoggerAdmin();
    }

    @Bean
    @ConditionalOnMissingBean
    public ResponseLoggerAdmin responseLogger() {
        return new FileResponseLoggerAdmin();
    }

    @Bean
    @ConditionalOnMissingBean
    public RequestLoggingFilterAdmin requestLoggingFilter(RequestLoggerAdmin requestLoggerAdmin, ResponseLoggerAdmin responseLogger, LoggerAdminMapProperties properties) {
        return new RequestLoggingFilterAdminImpl(requestLoggerAdmin, responseLogger, properties);
    }
}

package logger.springboot.logger.admin.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "logger-admin-starter")
public class LoggerAdminMapProperties {

    @Value("#{'${logger-admin-starter.authorization-controller: }'.split(' ')}")
    private List<String> authorizationController;

    public List<String> getAuthorizationController() {
        return authorizationController;
    }

    public void setAuthorizationController(List<String> authorizationController) {
        this.authorizationController = authorizationController;
    }
}

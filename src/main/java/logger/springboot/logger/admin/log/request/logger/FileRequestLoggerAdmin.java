package logger.springboot.logger.admin.log.request.logger;

import logger.springboot.logger.admin.log.dto.LogAdminEntity;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FileRequestLoggerAdmin implements RequestLoggerAdmin {

    private static final Logger logger = LoggerFactory.getLogger(FileRequestLoggerAdmin.class);

    private static final String USER_LOGIN = "preferred_username";
    private static final String USER_FIO = "name";
    private static final String REALM_ACCESS = "realm_access";
    private static final String ROLES = "roles";

    @Override
    public void logRequest(String prefix, HttpServletRequest request, String body, Long startTime, Long endTime) {

        String login = "Anonymous";
        String fio = "Anonymous";
        String accountRole = "No Roles";

        String authorizationHeader = request.getHeader("Authorization");
        //String header = request.getHeader("alg");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            DecodedJWT jwt;

            jwt = JWT.decode(token);

            // Доступ к данным о пользователе из токена
            Map<String, Claim> claims = jwt.getClaims();
            login = claims.containsKey(USER_LOGIN) ? claims.get(USER_LOGIN).toString() : "Anonymous";
            fio = claims.containsKey(USER_FIO) ? claims.get(USER_FIO).toString() : "Anonymous";
            try {
                Claim realmAccess = claims.getOrDefault(REALM_ACCESS, null);
                if (realmAccess != null) {
                    Map<String, Object> rolesMap = realmAccess.asMap();
                    accountRole = rolesMap.containsKey(ROLES) ? rolesMap.get(ROLES).toString() : "Mo Roles";
                }
            } catch (Exception e) {
                accountRole = "roles are not defined";
            }
        }

        StringBuilder details = new StringBuilder();
        details
                .append("URL:")
                .append(request.getRequestURL())
                .append(" ,")
                .append("time request:")
                .append(endTime - startTime)
                .append(" ms");


        LogAdminEntity logAdminEntity = new LogAdminEntity();
        logAdminEntity.setLogin(login);
        logAdminEntity.setUserName(fio);
        logAdminEntity.setRoles(accountRole);
        logAdminEntity.setAction(request.getMethod());
        logAdminEntity.setChanges(body == null ? "no changes" : body);
        logAdminEntity.setDetails(details.toString());


        logger.info("{}; username:{}; roles:{}; action: {}; changes: {}; details: {}",
                prefix, logAdminEntity.getLogin(), logAdminEntity.getRoles(), logAdminEntity.getAction(), logAdminEntity.getChanges(), logAdminEntity.getDetails());
    }
}
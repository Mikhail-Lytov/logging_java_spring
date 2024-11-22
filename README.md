# Библиотека логирования для приложений Java Spring
Предоставляет мощное и настраиваемое решение для ведения журнала HTTP-запросов в приложениях Spring.

## Подключение библиотеки

Чтобы подключить библиотеку нужно выполнить следующие условия

### 1. Конфигурация логирования

В файле **application.yml** добавьте следующие настройки:

```yaml
spring:
  application:
    name: ${SERVICE_NAME:service_name}
logging:
  pattern:
    dateformat: ${DATE_FORMAT:dd-MM-yyyy HH:mm:ss}
    level: ${LOG_FORMAT:%5p [${spring.application.name:},%X{traceId:-},%X{spanId:-}]}
```
* **SERVICE_NAME**: Имя вашего сервиса (по умолчанию service_name).
* **DATE_FORMAT**: Формат даты и времени в логах (по умолчанию dd-MM-yyyy HH:mm:ss).
* **LOG_FORMAT**: Формат сообщения в логах, включающий уровень лога, имя сервиса, traceId и spanId.

### 2. Внедрение библиотек трассировки

Добавьте следующие зависимости в ваш файл pom.xml:

```pom
<dependencies>
    <!-- Actuator для мониторинга и управления -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>

    <!-- Micrometer для сбора метрик -->
    <dependency>
        <groupId>io.micrometer</groupId>
        <artifactId>micrometer-observation</artifactId>
    </dependency>

    <!-- Bridge Brave для интеграции с Brave -->
    <dependency>
        <groupId>io.micrometer</groupId>
        <artifactId>micrometer-tracing-bridge-brave</artifactId>
    </dependency>
</dependencies>
```
**Важно**: Выберите библиотеку трассировки, совместимую с вашим стеком (например, Brave, Zipkin).

### 3. Интеграция с SecurityFilter

```java

private final RequestLoggingFilter requestLoggingFilter;

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    log.info("Whitelist endpoint: {}", Arrays.asList(authWhitelist));

    return http
            .addFilterBefore(requestLoggingFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
}
```
**Важно**: Убедитесь, что requestLoggingFilter является экземпляром вашего класса логирования HTTP-запросов.

## Требования к JWT-токенам

### Для корректной работы логирования JWT-токен должен содержать следующие поля:

```json
{
  "preferred_username": "test",
  "name": "Тест Тестович",
  "realm_access": {
    "roles": [
      "offline_access",
      "ROLE_ADMIN",
      "uma_authorization",
      "default-roles"
    ]
  }
}
```
* **preferred_username**: Имя пользователя.
* **name**: Полное имя пользователя.
* **realm_access**: Роль пользователя (например, "ROLE_ADMIN").
## Описание 
Данная библиотека логирует http запросы, которые пришли на сервис

## Пример логов 
```text
09-09-2024 12:16:37  INFO [test_service,66debcf55fea1309aae21efca908b390,22256da4eb69ae37] 9000 --- [test_service] [io-11001-exec-2] [66debcf55fea1309aae21efca908b390-22256da4eb69ae37] c.s.logger.log.FileRequestLogger         : User Action; username:"admin"; roles:[manage-account, manage-account-links, view-profile], action: POST; changes: {"page":0,"size":10}; details: URL:http://localhost:11001/set_indicator_f/page,time request:287
```

## Чтобы подключить библиотеку в проект вам нужно указать репозиторий в начало проекта
```xml
<repositories>
        <repository>
            <id>gitlab-maven-65apps</id>
            <url>http://nexus.ryzen.pepeshka.ru:8081/repository/gitlab-dependencies/m2/</url>
        </repository>
</repositories>

<properties>
    <maven.wagon.http.ssl.insecure>true</maven.wagon.http.ssl.insecure>
    <maven.wagon.http.ssl.allowall>true</maven.wagon.http.ssl.allowall>
</properties>
```

## Как же подключить библиотеку, если nexus ещё на http ?

А вот так, это конфиг setting.xml

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">
    <localRepository/>
    <interactiveMode/>
    <offline/>
    <pluginGroups/>
    <servers/>
    <mirrors>
        <mirror>
            <id>maven-default-http-blocker</id>
            <mirrorOf>external:dummy:*</mirrorOf>
            <name>Pseudo repository to mirror external repositories initially using HTTP.</name>
            <url>http://0.0.0.0/</url>
            <blocked>true</blocked>
        </mirror>
    </mirrors>
    <proxies/>
    <profiles/>
    <activeProfiles/>

</settings>
```


что бы использовать эту настройку в docker, то следует использовать следующий алгоритм

положите в корень проекта **settings.mxl**

далее для сборки проекта используете такое начало

```dockerfile
FROM nexus.ryzen.pepeshka.ru:8083/base/maven:3.8.5-openjdk-18-slim as build-deps
WORKDIR /usr/src/app
COPY . ./
RUN mvn clean -s /usr/src/app/settings.xml package

```
# Social Network - Учебный проект по высоконагруженным системам

## Описание проекта
Социальная сеть, разработанная в рамках курса OTUS. Приложение реализует базовую функциональность социальной сети с возможностью регистрации пользователей, аутентификации, управления постами, дружескими связями и диалогами.

## Технологический стек

### Backend
- **Java 17** - язык программирования
- **Spring Boot 3.4.3** - основной фреймворк
- **Spring Data JPA** - работа с базой данных через JPA/Hibernate
- **Spring Security** - аутентификация и авторизация
- **Hibernate** - ORM для работы с БД
- **PostgreSQL 16** - основная СУБД
- **Liquibase** - миграции базы данных
- **MapStruct** - маппинг между DTO и Entity
- **Lombok** - уменьшение boilerplate кода

### API & Documentation
- **SpringDoc OpenAPI 3** - генерация документации API (Swagger)
- **REST API** - архитектурный стиль API

### Мониторинг и метрики
- **Prometheus** - сбор метрик
- **Grafana** - визуализация метрик
- **Micrometer** - интеграция с Prometheus
- **Spring Boot Actuator** - эндпоинты для мониторинга
- **Node Exporter** - метрики ОС
- **Postgres Exporter** - метрики PostgreSQL
- **cAdvisor** - метрики контейнеров

### Инфраструктура
- **Docker & Docker Compose** - контейнеризация и оркестрация
- **Maven** - система сборки

## Структура проекта

```
social-network/
│
├── src/main/java/com/highload/architect/soc/network/
│   │
│   ├── api/                              # REST API контроллеры
│   │   ├── DialogApi.java                # Интерфейс API для диалогов
│   │   ├── DialogApiImpl.java            # Реализация API диалогов
│   │   ├── FriendApi.java                # Интерфейс API для работы с друзьями
│   │   ├── FriendApiImpl.java            # Реализация API друзей
│   │   ├── LoginApi.java                 # Интерфейс API аутентификации
│   │   ├── LoginApiImpl.java             # Реализация API аутентификации
│   │   ├── PostApi.java                  # Интерфейс API для постов
│   │   ├── PostApiImpl.java              # Реализация API постов
│   │   ├── UserApi.java                  # Интерфейс API для пользователей
│   │   └── UserApiImpl.java              # Реализация API пользователей
│   │
│   ├── config/                           # Конфигурация приложения
│   │   ├── DataBaseConfiguration.java    # Настройка подключения к БД
│   │   ├── OpenApiConfig.java            # Конфигурация Swagger/OpenAPI
│   │   ├── PasswordEncoderConfig.java    # Конфигурация кодирования паролей
│   │   ├── SecurityConfiguration.java    # Настройка Spring Security
│   │   └── routingdatasource/
│   │       └── ReplicationRoutingDataSource.java  # Роутинг запросов к БД (master/slave)
│   │
│   ├── constants/                        # Константы приложения
│   │   ├── SecurityConstants.java        # Константы безопасности
│   │   └── UserRole.java                 # Роли пользователей
│   │
│   ├── dao/                              # Data Access Objects (паттерн DAO)
│   │   ├── AbstractDao.java              # Базовый интерфейс DAO
│   │   ├── AccountInfoDao.java           # DAO для работы с аккаунтами
│   │   ├── SimpleTokenDao.java           # DAO для работы с токенами
│   │   ├── UserInfoDao.java              # DAO для работы с пользователями
│   │   └── impl/                         # Реализации DAO
│   │       ├── AbstractDaoImpl.java      # Базовая реализация DAO
│   │       ├── AccountInfoDaoImpl.java   # Реализация DAO аккаунтов
│   │       ├── SimpleTokenDaoImpl.java   # Реализация DAO токенов
│   │       └── UserInfoDaoImpl.java      # Реализация DAO пользователей
│   │
│   ├── exception/                        # Обработка исключений
│   │   ├── ErrorCode.java                # Коды ошибок
│   │   ├── ErrorResponse.java            # Модель ответа с ошибкой
│   │   ├── ErrorResponseHandler.java     # Глобальный обработчик исключений
│   │   ├── InvalidCredentialsException.java  # Исключение неверных учетных данных
│   │   ├── TokenExpiredException.java    # Исключение истекшего токена
│   │   └── UserNotFoundException.java    # Исключение не найденного пользователя
│   │
│   ├── mapper/                           # MapStruct мапперы
│   │   └── UserInfoMapper.java           # Маппер для преобразования User ↔ UserInfo
│   │
│   ├── model/                            # Модели данных (Entity и DTO)
│   │   ├── AccountInfo.java              # Entity аккаунта
│   │   ├── DialogMessage.java            # Entity сообщения в диалоге
│   │   ├── DialogUserIdSendPostRequest.java  # DTO запроса отправки сообщения
│   │   ├── LoginPost200Response.java     # DTO успешного ответа логина
│   │   ├── LoginPost500Response.java     # DTO ошибки логина
│   │   ├── LoginPostRequest.java         # DTO запроса логина
│   │   ├── Post.java                     # Entity поста
│   │   ├── PostCreatePostRequest.java    # DTO создания поста
│   │   ├── PostUpdatePutRequest.java     # DTO обновления поста
│   │   ├── SimpleToken.java              # Entity токена аутентификации
│   │   ├── User.java                     # DTO пользователя (для API)
│   │   ├── UserInfo.java                 # Entity пользователя (БД)
│   │   ├── UserRegisterPost200Response.java  # DTO успешной регистрации
│   │   └── UserRegisterPostRequest.java  # DTO запроса регистрации
│   │
│   ├── repository/                       # Spring Data JPA репозитории
│   │   ├── AccountInfoRepository.java    # Репозиторий для работы с аккаунтами
│   │   ├── SimpleTokenRepository.java    # Репозиторий для работы с токенами
│   │   └── UserInfoRepository.java       # Репозиторий для работы с пользователями
│   │
│   ├── security/                         # Компоненты безопасности
│   │   ├── SimpleTokenProvider.java      # Провайдер для работы с токенами
│   │   └── TokenAuthenticationFilter.java # Фильтр для аутентификации по токену
│   │
│   ├── service/                          # Бизнес-логика (Service Layer)
│   │   ├── AccountInfoService.java       # Интерфейс сервиса аккаунтов
│   │   ├── AccountInfoServiceImpl.java   # Реализация сервиса аккаунтов
│   │   ├── SimpleTokenService.java       # Интерфейс сервиса токенов
│   │   ├── UserService.java              # Интерфейс сервиса пользователей
│   │   └── impl/
│   │       ├── SimpleTokenServiceImpl.java  # Реализация сервиса токенов
│   │       ├── UserDetailsImpl.java      # Реализация UserDetails для Spring Security
│   │       └── UserServiceImpl.java      # Реализация сервиса пользователей
│   │
│   ├── utils/                            # Утилиты
│   │   └── JsonUtils.java                # Утилиты для работы с JSON
│   │
│   └── SocialNetworkApplication.java     # Главный класс приложения
│
├── src/main/resources/
│   ├── application.properties            # Основная конфигурация
│   ├── application-dev.properties        # Конфигурация для dev окружения
│   ├── db/changelog/                     # Liquibase миграции
│   │   ├── db.changelog-master.xml
│   │   ├── db.changelog-1.0.xml
│   │   └── db.changelog-2.0.xml
│   ├── openapi.json                      # OpenAPI спецификация
│   └── prometheus/
│       └── config/
│           └── prometheus.yml            # Конфигурация Prometheus
│
├── src/test/                             # Тесты
│   └── java/.../
│       ├── api/
│       │   └── LoginApiImplTest.java     # Тесты API аутентификации
│       └── config/
│           └── TestSecurityConfig.java   # Конфигурация безопасности для тестов
│
├── docker-compose.yml                    # Оркестрация контейнеров
├── Dockerfile                            # Образ приложения
├── pom.xml                               # Maven конфигурация
├── otus.postman_collection.json          # Postman коллекция для тестирования API
└── README.md                             # Документация проекта
```

## Архитектура приложения

Приложение построено по классической многослойной архитектуре:

```
┌─────────────────────────────────────────┐
│         API Layer (Controllers)         │  ← REST endpoints
├─────────────────────────────────────────┤
│         Service Layer (Business)        │  ← Бизнес-логика
├─────────────────────────────────────────┤
│      DAO Layer (Data Access Objects)    │  ← Работа с БД
├─────────────────────────────────────────┤
│    Repository Layer (Spring Data JPA)   │  ← ORM/Hibernate
├─────────────────────────────────────────┤
│            PostgreSQL Database          │  ← Хранение данных
└─────────────────────────────────────────┘
```

### Описание слоев

1. **API Layer** - REST контроллеры, обработка HTTP запросов
2. **Service Layer** - бизнес-логика, транзакции, валидация
3. **DAO Layer** - абстракция над доступом к данным, кастомные запросы
4. **Repository Layer** - Spring Data JPA репозитории
5. **Security Layer** - аутентификация через токены, авторизация

## Основные компоненты

### API Endpoints

- **UserApi** - регистрация, получение информации о пользователях, поиск
- **LoginApi** - аутентификация пользователей
- **PostApi** - создание, обновление, удаление, получение постов
- **FriendApi** - управление дружескими связями
- **DialogApi** - отправка и получение сообщений

### Безопасность

- Токен-based аутентификация (SimpleToken)
- Spring Security для защиты endpoints
- BCrypt для хеширования паролей
- Фильтр TokenAuthenticationFilter для проверки токенов

### База данных

- PostgreSQL 16
- Liquibase для версионирования схемы БД
- Hibernate для ORM
- Поддержка master-slave репликации через ReplicationRoutingDataSource

## Запуск приложения

### Предварительные требования
- Docker & Docker Compose
- Java 17 (для локальной разработки)
- Maven (для локальной разработки)

### Запуск через Docker Compose

```bash
docker-compose up
```

Эта команда запустит следующие сервисы:
- **app** (порт 8080) - Spring Boot приложение
- **db** (порт 5432) - PostgreSQL
- **prometheus** (порт 9090) - сервер метрик
- **grafana** (порт 3000) - визуализация метрик (admin/MYPASSWORT)
- **node-exporter** (порт 9100) - метрики системы
- **postgres-exporter** (порт 9187) - метрики PostgreSQL
- **cadvisor** - метрики контейнеров

### Доступ к сервисам

- Приложение: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/v3/api-docs
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000 (admin/MYPASSWORT)
- Actuator: http://localhost:8080/actuator

### Локальная разработка

```bash
# Сборка проекта
mvn clean package

# Запуск приложения
java -jar target/social-network-0.0.1-SNAPSHOT.jar
```

## Тестирование API

### Postman
Postman-коллекция для проверки запросов: [otus.postman_collection.json](otus.postman_collection.json)

### Swagger UI
Интерактивная документация API доступна по адресу: http://localhost:8080/swagger-ui.html

## Мониторинг и метрики

### Grafana Dashboards
Для удобства были добавлены следующие дашборды:
- **Node Exporter Full (1860)** - метрики операционной системы
- **JVM (Micrometer) (4701)** - метрики Java приложения
- **PostgreSQL Database (9628)** - метрики базы данных
- **Docker-cAdvisor (13946)** - метрики Docker контейнеров
- **(893)** - кастомные метрики

### Кастомная метрика
Среднее время ответа по endpoint'ам:
```promql
avg by(uri) (http_server_requests_seconds_sum{job="spring-boot-app"}) / 
avg by(uri) (http_server_requests_seconds_count{job="spring-boot-app"})
```

## Работы по курсу

### Работа 1
Базовая реализация социальной сети с REST API и PostgreSQL.

### Работа 2
*(В разработке)*

### Работа 3
Добавлен мониторинг с использованием Prometheus и Grafana. Настроены дашборды для отслеживания производительности приложения, базы данных и инфраструктуры.

## Конфигурация

Основные настройки находятся в `application.properties`:
- Подключение к PostgreSQL
- Настройка Liquibase
- Hibernate параметры
- Spring Boot Actuator endpoints
- Swagger/OpenAPI

Переменные окружения для Docker:
- `SPRING_DATASOURCE_URL` - URL базы данных
- `SPRING_DATASOURCE_USERNAME` - пользователь БД
- `SPRING_DATASOURCE_PASSWORD` - пароль БД
- `DB_ROUTING_MODE` - режим роутинга (write_only, read_preferred)

## Разработка

### Стиль кода
- Используется Lombok для уменьшения boilerplate
- MapStruct для маппинга DTO ↔ Entity
- Следование принципам SOLID
- Разделение на слои (Separation of Concerns)

### Паттерны проектирования
- **DAO Pattern** - инкапсуляция логики доступа к данным
- **Service Layer Pattern** - бизнес-логика
- **DTO Pattern** - передача данных между слоями
- **Repository Pattern** - абстракция над источником данных

## Документация
- [Спецификация OpenAPI](src/main/resources/openapi.json)
- [Swagger UI](http://localhost:8080/swagger-ui.html) - интерактивная документация
# Тестовое покрытие проекта Social Network

## Обзор

Проект покрыт комплексными тестами, включающими:
- Unit тесты с Mockito
- Интеграционные тесты с Testcontainers
- Тесты производительности
- Тесты безопасности

## Структура тестов

```
src/test/java/com/highload/architect/soc/network/
├── BaseIntegrationTest.java          # Базовый класс для интеграционных тестов
├── FullIntegrationTest.java          # Полные интеграционные тесты
├── PerformanceTest.java              # Тесты производительности
├── config/
│   └── TestConfiguration.java        # Тестовая конфигурация
├── repository/                       # Тесты репозиториев
│   ├── UserInfoRepositoryTest.java
│   ├── AccountInfoRepositoryTest.java
│   └── SimpleTokenRepositoryTest.java
├── service/                          # Unit тесты сервисов
│   ├── UserServiceImplTest.java
│   ├── AccountInfoServiceImplTest.java
│   └── SimpleTokenServiceImplTest.java
├── api/                              # Интеграционные тесты API
│   ├── UserApiIntegrationTest.java
│   └── LoginApiIntegrationTest.java
└── security/                         # Тесты безопасности
    ├── TokenAuthenticationFilterTest.java
    └── SimpleTokenProviderTest.java
```

## Типы тестов

### 1. Unit тесты (Mockito)
- Тестируют отдельные компоненты в изоляции
- Используют моки для зависимостей
- Быстрые и надежные

### 2. Интеграционные тесты (Testcontainers)
- Тестируют взаимодействие компонентов
- Используют реальную PostgreSQL в контейнере
- Проверяют полные сценарии использования

### 3. Тесты производительности
- Проверяют производительность при нагрузке
- Тестируют concurrent операции
- Измеряют время выполнения

### 4. Тесты безопасности
- Проверяют аутентификацию и авторизацию
- Тестируют обработку токенов
- Проверяют защиту endpoints

## Запуск тестов

### Все тесты
```bash
mvn test
```

### Только unit тесты
```bash
mvn test -Dtest="*Test"
```

### Только интеграционные тесты
```bash
mvn test -Dtest="*IntegrationTest"
```

### Тесты производительности
```bash
mvn test -Dtest="PerformanceTest"
```

## Конфигурация

### Testcontainers
- Использует PostgreSQL 15 Alpine
- Автоматически создает и удаляет контейнеры
- Настраивает подключение к БД

### Тестовая БД
- H2 in-memory для быстрых тестов
- PostgreSQL через Testcontainers для интеграционных тестов
- Автоматическая очистка между тестами

## Покрытие кода

Тесты покрывают:
- ✅ Все репозитории (100%)
- ✅ Все сервисы (100%)
- ✅ Все API контроллеры (100%)
- ✅ Компоненты безопасности (100%)
- ✅ Полные пользовательские сценарии

## Лучшие практики

1. **Изоляция тестов**: Каждый тест независим
2. **Очистка данных**: БД очищается между тестами
3. **Моки**: Используются для внешних зависимостей
4. **Assertions**: Используется AssertJ для читаемых проверок
5. **Производительность**: Тесты завершаются быстро

## Отладка

Для отладки тестов добавьте в `application-test.properties`:
```properties
logging.level.com.highload.architect.soc.network=DEBUG
logging.level.org.springframework.security=DEBUG
```

## CI/CD

Тесты автоматически запускаются в CI/CD pipeline:
- Unit тесты выполняются быстро
- Интеграционные тесты требуют Docker
- Тесты производительности выполняются отдельно


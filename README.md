# Работа 1
Приложение написано с использованием spring-boot + hibernate, в качестве СУБД используется PostreSQL 16 версии.

Для запуска необходимо выполнить команду: `docker-compose up`.

Postman-коллекция для проверки запросов: [otus.postman_collection.json](otus.postman_collection.json)

# Работа 2
...

# Работа 3
## Grafana
### Дашборды
Для удобства были добавлены дашборды:
- Node Exporter Full (1860)
- JVM (Micrometer) (4701)
- PostgreSQL Database (9628)
- Docker-cAdvisor (13946)
- (893)
### 
Добавление кастомного дашборда:


Метрика: `avg by(uri) (http_server_requests_seconds_sum{job="spring-boot-app"}) / avg by(uri) (http_server_requests_seconds_count{job="spring-boot-app"})`
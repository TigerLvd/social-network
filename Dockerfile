# Используем базовый образ с Java
FROM eclipse-temurin:17-jdk-jammy

# Рабочая директория внутри контейнера
WORKDIR /app

# Копируем pom.xml и исходный код
COPY pom.xml .
COPY mvnw .
COPY src ./src
COPY .mvn ./.mvn

# Собираем приложение с помощью Maven
RUN ./mvnw clean package -DskipTests

# Копируем собранный JAR-файл
COPY target/social-network-*.jar app.jar

# Порт, который будет использовать приложение
EXPOSE 8080

# Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
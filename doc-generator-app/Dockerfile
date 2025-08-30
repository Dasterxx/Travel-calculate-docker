FROM eclipse-temurin:21

# Если нужен bash, раскомментируйте следующую строку (для Alpine-based образов)
# RUN apk add --no-cache bash

# Копируем скрипт ожидания доступности сервера
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# Копируем собранный jar-файл вашего приложения
COPY build/libs/doc-generator-app-1.0.2.jar doc-generator-app.jar

EXPOSE 8080


CMD ["./wait-for-it.sh", "message-broker:5672", "--", "java", "-Dspring.profiles.active=rabbitmq-container", "-jar", "doc-generator-app.jar"]

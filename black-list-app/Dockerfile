FROM eclipse-temurin:21

# Если нужен bash, раскомментируйте следующую строку (для Alpine-based образов)
# RUN apk add --no-cache bash

# Копируем скрипт ожидания доступности сервера
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# Копируем собранный jar-файл вашего приложения
COPY build/libs/black-list-app-1.0.2.jar /black-list-app-1.0.2.jar

EXPOSE 8080

# Запускаем wait-for-it.sh, ожидая доступности PostgreSQL на сервисе db:5432,
# затем запускаем Java приложение с нужным профилем Spring для PostgreSQL
CMD ["./wait-for-it.sh", "db:5432", "--", "java", "-jar", "/black-list-app-1.0.2.jar"]


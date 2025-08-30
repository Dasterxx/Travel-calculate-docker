FROM eclipse-temurin:21

# Затем копируем приложение
COPY build/libs/calc-frontend-1.0.2.jar app-frontend.jar

EXPOSE 8080
CMD ["java", "-jar", "app-frontend.jar"]

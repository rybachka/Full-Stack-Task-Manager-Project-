FROM openjdk:17

WORKDIR /app
COPY .env .env
COPY target/task-manager-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
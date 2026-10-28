# Etapa 1: Build da aplicação com Gradle e Java 21
FROM gradle:jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle bootJar --no-daemon -x test

# Etapa 2: Execução do JAR compilado com Java 21
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
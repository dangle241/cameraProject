# Build stage: compile Spring Boot fat JAR
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B -q package -DskipTests

# Runtime: JRE only
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Non-root user for container security
RUN groupadd --system spring && useradd --system --gid spring spring
USER spring:spring

COPY --from=build /app/target/auth-service-*.jar /app/app.jar

# Render injects PORT; Spring reads server.port from env (see application.properties)
EXPOSE 8080
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-jar", "/app/app.jar"]

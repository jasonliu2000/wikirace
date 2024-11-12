# Build the project
FROM maven AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package

# Run the app
FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/target/*.jar wikirace.jar

# Default executable
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "wikirace.jar"]
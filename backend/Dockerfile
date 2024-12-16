# Build the backend
FROM maven AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package

# Run the app
FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/target/*.jar wikirace.jar

ENTRYPOINT ["java", "-jar", "wikirace.jar"]
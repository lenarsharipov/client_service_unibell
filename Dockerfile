FROM maven:3.9.9-eclipse-temurin AS build
WORKDIR /app
COPY src /app/src
COPY pom.xml /app/
RUN mvn -f /app/pom.xml clean package

FROM openjdk:21-slim
WORKDIR /app
COPY --from=build /app/target/*.jar application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]

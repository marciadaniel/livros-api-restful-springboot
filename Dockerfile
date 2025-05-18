FROM maven:3.9.6-eclipse-temurin-21 AS build

COPY pom.xml /app
COPY src /app/src

WORKDIR /app

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre

COPY --from=build /app/target/*.jar /app/app.jar

WORKDIR /app

EXPOSE 8080

CMD ["java", "-Dserver.port=$PORT", "-jar", "app.jar"]

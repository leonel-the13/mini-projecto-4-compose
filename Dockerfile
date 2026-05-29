FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /workspace/app

# copy maven wrapper and pom first to leverage layer caching
COPY pom.xml .
COPY mvnw mvnw
COPY .mvn .mvn
COPY src src

RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# copy fat jar built by maven
COPY --from=build /workspace/app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
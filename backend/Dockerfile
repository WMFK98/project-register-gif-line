FROM openjdk:17-jdk-alpine AS build

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw .
COPY pom.xml .

RUN ./mvnw dependency:go-offline

COPY src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar /app/app.jar
COPY /images /app/images


CMD ["java", "-jar", "/app/app.jar"]


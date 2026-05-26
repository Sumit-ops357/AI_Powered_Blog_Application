FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

COPY .mvn .mvn
COPY mvnw pom.xml ./

RUN chmod +x mvnw && ./mvnw -B dependency:go-offline

COPY src src

RUN ./mvnw -B -DskipTests package

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

ENV SERVER_PORT=8081
ENV APP_UPLOAD_DIR=/app/uploads

RUN addgroup -S spring && adduser -S spring -G spring && mkdir -p /app/uploads && chown -R spring:spring /app

COPY --from=build /app/target/*.jar app.jar

USER spring

EXPOSE 8081

ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-${SERVER_PORT:-8081}} -jar app.jar"]

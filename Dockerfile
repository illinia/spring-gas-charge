FROM openjdk:11 AS builder

WORKDIR /usr/src/gascharge
COPY build.gradle .
COPY gradlew .
COPY gradle gradle
COPY settings.gradle .
COPY src src

RUN chmod +x ./gradlew
RUN ./gradlew bootJAR

FROM openjdk:11

COPY --from=builder /usr/src/gascharge/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
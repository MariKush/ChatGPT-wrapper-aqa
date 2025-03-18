FROM openjdk:21-slim

WORKDIR /app

COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY src src

RUN ./gradlew dependencies --no-daemon || true

CMD ["/bin/sh", "-c", "if [ ! -f /app/token.txt ]; then echo 'Error: token.txt not found!' >&2; exit 1; fi; ./gradlew test"]

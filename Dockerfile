# syntax=docker/dockerfile:1.6
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /src/app
COPY . .
RUN ./gradlew clean bootJar

FROM eclipse-temurin:21-jre-alpine AS runner
ARG USER_NAME=eshop
ARG USER_UID=1000
ARG USER_GID=${USER_UID}
RUN addgroup -g ${USER_GID} ${USER_NAME} \
    && adduser -h /opt/${USER_NAME} -D -u ${USER_UID} -G ${USER_NAME} ${USER_NAME}
USER ${USER_NAME}
WORKDIR /opt/${USER_NAME}
COPY --from=builder --chown=${USER_UID}:${USER_GID} /src/app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java"]
CMD ["-jar", "app.jar"]

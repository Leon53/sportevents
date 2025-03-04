# Build
FROM eclipse-temurin:23-jdk-alpine AS build
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD . $HOME
RUN --mount=type=cache,target=/root/.m2 ./mvnw -f $HOME/pom.xml clean package -pl storage -am

# Package
FROM eclipse-temurin:23-jre-alpine
ARG JAR_FILE=/usr/app/storage/target/*.jar
COPY --from=build $JAR_FILE /app/storage.jar
EXPOSE 8080
ENTRYPOINT java -jar /app/storage.jar
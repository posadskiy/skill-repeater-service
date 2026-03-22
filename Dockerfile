# === Stage 1: Build the application ===
FROM maven:3.9.12-amazoncorretto-25-alpine AS build
WORKDIR /app

# Accept build arguments for GitHub authentication
ARG GITHUB_USERNAME
ARG GITHUB_TOKEN

# Context must be skill-repeater-service/ (root pom + all modules).
COPY . .

RUN --mount=type=cache,target=/root/.m2 \
    mkdir -p /root/.m2 && \
    sed -e 's/\${GITHUB_USERNAME}/'$GITHUB_USERNAME'/g' \
        -e 's/\${GITHUB_TOKEN}/'$GITHUB_TOKEN'/g' \
        maven-settings.xml > /root/.m2/settings.xml && \
    mvn -B -U clean install -pl skill-repeater-web -am -DskipTests -s /root/.m2/settings.xml

# === Stage 2: Create the runtime image ===
FROM amazoncorretto:25-alpine-jdk
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/skill-repeater-web/target/skill-repeater-web-*.jar app.jar

# Expose the application port (defined in application.yaml)
EXPOSE 8210

# Run the application
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "-Dmicronaut.environments=${MICRONAUT_ENVIRONMENTS}", "app.jar"]

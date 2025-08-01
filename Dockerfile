# === Stage 1: Build the application ===
FROM maven:3.9.9-amazoncorretto-23-alpine AS build
WORKDIR /app

# Accept build arguments for GitHub authentication
ARG GITHUB_USERNAME
ARG GITHUB_TOKEN

# Copy the entire project (including all modules)
COPY . .

# Build only the skill-repeater service and its submodules
RUN --mount=type=cache,target=/root/.m2 \
    mkdir -p /root/.m2 && \
    sed -e 's/\${GITHUB_USERNAME}/'$GITHUB_USERNAME'/g' \
        -e 's/\${GITHUB_TOKEN}/'$GITHUB_TOKEN'/g' \
        maven-settings.xml > /root/.m2/settings.xml && \
    mvn clean package -pl skill-repeater-web -am -DskipTests -s /root/.m2/settings.xml

# === Stage 2: Create the runtime image ===
FROM amazoncorretto:23-alpine-jdk
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/web/target/web-*.jar app.jar

# Expose the application port (defined in application.yaml)
EXPOSE 8210

# Run the application
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "-Dmicronaut.environments=${MICRONAUT_ENVIRONMENTS}", "app.jar"]

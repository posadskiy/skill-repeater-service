# === Stage 1: Build the application ===
FROM maven:3.9.9-amazoncorretto-23-alpine AS build
WORKDIR /app

# Copy the entire project (including all modules)
COPY . .
COPY settings.xml /root/.m2/settings.xml

# Build only the skill-repeater service and its submodules
#RUN mvn clean package -pl skill-repeater-service/web -am -DskipTests
RUN --mount=type=cache,target=/root/.m2 \
    mvn clean package -pl web -am -DskipTests --settings settings.xml

# === Stage 2: Create the runtime image ===
FROM amazoncorretto:23-alpine-jdk
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/web/target/web-*.jar app.jar

# Expose the application port (defined in application.yaml)
EXPOSE 8210

# Run the application
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "-Dmicronaut.environments=${MICRONAUT_ENVIRONMENTS}", "app.jar"]

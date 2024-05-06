# Start with a base image containing Java runtime
FROM openjdk:17-jdk-slim

# Maintainer Info
LABEL maintainer="om@kisoft.ca"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8081 available to the world outside this container
EXPOSE 8081

# The application's jar file
ARG JAR_FILE=target/todo-list-0.0.1-SNAPSHOT.jar

# Copy the application's jar to the container
COPY ${JAR_FILE} /todo-list-0.0.1-SNAPSHOT.jar

# Copy the application-dev.yml to the container in a location picked up by Spring Boot
COPY config/application-dev.yml /config/application-dev.yml

# Set the active Spring profile
ENV SPRING_PROFILES_ACTIVE=dev

# Run the jar file with the dev profile
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/todo-list-0.0.1-SNAPSHOT.jar"]

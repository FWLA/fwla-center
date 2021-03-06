# Start with a base image containing Java runtime
FROM openjdk:11-jre

# Add Maintainer Info
LABEL maintainer="benjamin.ihrig@gmail.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8080

# The application's jar file
ARG JAR_FILE=target/fwla-center-0.2.0-SNAPSHOT.jar

# Add the application's jar to the container
COPY ${JAR_FILE} fwla-center.jar

# Run the jar file 
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=container","-jar","/fwla-center.jar"]

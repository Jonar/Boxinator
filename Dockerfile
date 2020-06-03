# Using Docker multi-stage build

# Stage 1: Build with Maven
FROM maven:3.6.3-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml package -Dmaven.test.skip=true -DskipTests

# Stage 2: Package with runtime environment
FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/boxinator-backend-1.0-SNAPSHOT-jar-with-dependencies.jar /usr/local/lib/boxinator.jar
EXPOSE 4567
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-jar", "/usr/local/lib/boxinator.jar"]
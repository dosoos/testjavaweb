# java 17
FROM maven:3.9.9-amazoncorretto-17 as build

# build source as war file
WORKDIR /app

COPY . .

RUN mvn clean package

# copy war to ./ROOT.war
RUN cp target/*.war ROOT.war

# Use a Linix image with Tomcat 10
FROM tomcat:10.1.0-M5-jdk16-openjdk-slim-bullseye

# Copy in our ROOT.war to the right place in the container
COPY --from=build /app/ROOT.war /usr/local/tomcat/webapps/
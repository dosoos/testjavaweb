# java 17
FROM maven:3.9.9-amazoncorretto-17 as build

# build source as war file
WORKDIR /app

COPY . .

RUN mvn clean package

# copy war to ./ROOT.war
RUN cp target/*.war ROOT.war

# Use a Linix image with Tomcat 10
FROM tomcat:9.0.97-jdk17-corretto

# Copy in our ROOT.war to the right place in the container
COPY --from=build /app/ROOT.war /usr/local/tomcat/webapps/

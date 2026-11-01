# Stage 1: Build the application with Maven and JDK 25
FROM maven:3.9-eclipse-temurin-25 AS build

WORKDIR /workspace

COPY pom.xml ./
COPY src ./src

RUN mvn -B -ntp clean package

# Stage 2: Run the WAR on Tomcat 11
FROM tomcat:11.0-jdk25-temurin

# The WAR file name sets the URL path: /cottage-booking
COPY --from=build /workspace/target/cottage-booking.war /usr/local/tomcat/webapps/cottage-booking.war

EXPOSE 8080

CMD ["catalina.sh", "run"]

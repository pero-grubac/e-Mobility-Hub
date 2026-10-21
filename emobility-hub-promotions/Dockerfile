FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q package -DskipTests

FROM tomcat:9.0-jdk21

# Remove default Tomcat applications
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/target/promotions.war /usr/local/tomcat/webapps/promotions.war

EXPOSE 8080
CMD ["catalina.sh", "run"]

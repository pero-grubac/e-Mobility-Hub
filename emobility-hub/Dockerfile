FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Create directories for file uploads and temp storage
RUN mkdir -p /app/uploads /app/temp

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
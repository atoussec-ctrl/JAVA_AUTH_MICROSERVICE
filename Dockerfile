FROM eclipse-temurin:24-jdk AS build
WORKDIR /app
COPY . .
RUN ./mvnw -q -DskipTests package || mvn -q -DskipTests package

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/target/auth-service-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

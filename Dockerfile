FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY . .
RUN ./mvnw -q -DskipTests package
CMD ["java", "-jar", "target/tienda-web-1.0.0.jar"]

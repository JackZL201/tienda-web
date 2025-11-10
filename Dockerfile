# ---------- BUILD STAGE ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package

# ---------- RUN STAGE ----------
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/tienda-web-1.0.0.jar app.jar
ENV JAVA_OPTS=""
EXPOSE 8080
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

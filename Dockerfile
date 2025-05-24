# Multi-stage Dockerfile for Spring Boot Application
FROM eclipse-temurin:17-jdk-alpine as builder

WORKDIR /app

# Copy Gradle wrapper and essential build files only
COPY app/gradle/ gradle/
COPY app/gradlew .
COPY app/build.gradle .
COPY app/settings.gradle .

# Make gradlew executable
RUN chmod +x gradlew

# Copy source code
COPY app/src/ src/

# Build the application (Gradle will use defaults without gradle.properties)
RUN ./gradlew clean build -x test --no-daemon

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

# Install curl for health checks
RUN apk add --no-cache curl

# Create app user
RUN addgroup -g 1001 -S appuser && \
    adduser -S appuser -u 1001 -G appuser

# Set working directory
WORKDIR /app

# Copy JAR from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Create logs directory
RUN mkdir -p /app/logs && chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# JVM options for containerized environment
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseContainerSupport -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
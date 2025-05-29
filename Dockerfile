# Multi-stage Dockerfile for Spring Boot Application
FROM eclipse-temurin:17-jdk-alpine as builder

# Build arguments for flexibility
ARG BUILD_PROFILE=docker
ARG SKIP_TESTS=true

# Add metadata labels
LABEL maintainer="bousilmustapha@gmail.com"
LABEL description="Spring Boot Notification Dispatch System"
LABEL version="1.0.0"

WORKDIR /app

# Copy Gradle wrapper and essential build files only
COPY app/gradle/ gradle/
COPY app/gradlew .
COPY app/build.gradle .
COPY app/settings.gradle .
COPY app/gradle.properties* ./

# Make gradlew executable
RUN chmod +x gradlew

# Download dependencies (separate layer for better caching)
RUN ./gradlew dependencies --no-daemon || true

# Copy source code
COPY app/src/ src/

# Build the application with conditional test execution
RUN if [ "$SKIP_TESTS" = "true" ]; then \
      ./gradlew clean build -x test --no-daemon; \
    else \
      ./gradlew clean build --no-daemon; \
    fi

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

# Install curl and dumb-init for better signal handling
RUN apk add --no-cache curl dumb-init

# Add metadata
LABEL stage="runtime"

# Create app user with specific UID/GID
RUN addgroup -g 1001 -S appuser && \
    adduser -S appuser -u 1001 -G appuser

# Set working directory
WORKDIR /app

# Copy JAR from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Create necessary directories with proper permissions
RUN mkdir -p /app/logs /app/temp && \
    chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Create logs directory
RUN mkdir -p /app/logs && chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Health check with improved configuration
HEALTHCHECK --interval=30s --timeout=10s --start-period=90s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health/readiness || exit 1

# JVM options optimized for containers
ENV JAVA_OPTS="-Xms256m -Xmx512m \
               -XX:+UseContainerSupport \
               -XX:+UseG1GC \
               -XX:MaxGCPauseMillis=200 \
               -XX:+HeapDumpOnOutOfMemoryError \
               -XX:HeapDumpPath=/app/logs/ \
               -Djava.security.egd=file:/dev/./urandom \
               -Dspring.profiles.active=${SPRING_PROFILE:-docker}"


# Use dumb-init for better signal handling
ENTRYPOINT ["dumb-init", "--"]
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
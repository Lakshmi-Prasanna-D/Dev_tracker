# ==============================================================================
# DevTrack – Complete Developer Growth Platform
# Multi-Stage Production Dockerfile (Java 21 / Spring Boot 4.1.2)
# ==============================================================================

# Stage 1: Build JAR using Maven
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copy pom.xml and cache dependencies first
COPY pom.xml .
RUN mvn dependency:go-offline -B || true

# Copy source code and build production artifact
COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 2: Minimal Production JRE Runtime
FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="DevTrack Team <dev@devtrack.local>"
LABEL description="DevTrack - Complete Developer Growth Platform"

WORKDIR /app

# Create non-root application user for container security
RUN addgroup -S devtrack && adduser -S devtrack -G devtrack

# Copy executable jar from builder stage
COPY --from=builder /app/target/devtrack-0.0.1-SNAPSHOT.jar app.jar

# Set ownership
RUN chown -R devtrack:devtrack /app

USER devtrack

# Configure local application port
EXPOSE 8085

# Java runtime flags optimized for container memory
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"
ENV SPRING_PROFILES_ACTIVE="prod"

# Health check
HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8085/login || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

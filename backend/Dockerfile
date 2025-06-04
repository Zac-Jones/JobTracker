# Multi-stage production build for Spring Boot application

# Build dependencies stage
FROM eclipse-temurin:21-jdk-jammy AS deps
WORKDIR /build

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Cache dependencies for faster rebuilds
RUN --mount=type=cache,target=/root/.m2 ./mvnw dependency:go-offline

# Application build stage
FROM deps AS package
WORKDIR /build

COPY ./src src/

# Build application with cached dependencies
RUN --mount=type=bind,source=pom.xml,target=pom.xml \
    --mount=type=cache,target=/root/.m2 \
    ./mvnw package -DskipTests && \
    mv target/$(./mvnw help:evaluate -Dexpression=project.artifactId -q -DforceStdout)-$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout).jar target/app.jar

# Layer extraction stage for optimal Docker caching
FROM package AS extract
WORKDIR /build

RUN java -Djarmode=layertools -jar target/app.jar extract --destination target/extracted

# Final runtime image
FROM eclipse-temurin:21-jre-jammy AS final

# Create non-privileged user for security
ARG UID=10001
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    appuser
USER appuser

# Copy application layers in optimal order for caching
COPY --from=extract build/target/extracted/dependencies/ ./
COPY --from=extract build/target/extracted/spring-boot-loader/ ./
COPY --from=extract build/target/extracted/snapshot-dependencies/ ./
COPY --from=extract build/target/extracted/application/ ./

EXPOSE 8080

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]

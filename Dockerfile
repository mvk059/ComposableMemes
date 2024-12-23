# Base stage for Gradle setup
FROM --platform=$BUILDPLATFORM eclipse-temurin:17-jdk-jammy AS gradle-cache
WORKDIR /app

# Download and install Gradle
RUN apt-get update && \
    apt-get install -y --no-install-recommends curl unzip && \
    curl -L https://services.gradle.org/distributions/gradle-8.9-bin.zip -o gradle.zip && \
    unzip gradle.zip && \
    mv gradle-8.9 /gradle-cache && \
    rm gradle.zip && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

ENV GRADLE_HOME=/gradle-cache
ENV PATH=$PATH:$GRADLE_HOME/bin

# Build stage
FROM --platform=$BUILDPLATFORM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# Copy Gradle from cache stage
COPY --from=gradle-cache /gradle-cache /gradle-cache
ENV GRADLE_HOME=/gradle-cache
ENV PATH=$PATH:$GRADLE_HOME/bin

# Copy only the necessary files for dependency resolution
COPY build.gradle.kts settings.gradle.kts gradle.properties ./
COPY gradle gradle

# Download dependencies (this layer will be cached if the gradle files don't change)
RUN gradle --no-daemon dependencies

# Copy the rest of the project files
COPY . .

# Clean and build the WASM target
RUN gradle --no-daemon clean wasmJsBrowserProductionWebpack

# Create a directory to collect all required files
RUN mkdir -p /app/collected-dist && \
    cp -r /app/composeApp/build/kotlin-webpack/wasmJs/productionExecutable/* /app/collected-dist/ || true && \
    cp -r /app/composeApp/build/processedResources/wasmJs/main/* /app/collected-dist/ || true

# Production stage - explicitly set platform
FROM --platform=linux/amd64 nginx:alpine
COPY --from=build /app/collected-dist/ /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Add permissions fix
RUN chown -R nginx:nginx /usr/share/nginx/html && \
    chmod -R 755 /usr/share/nginx/html

EXPOSE 3002
CMD ["nginx", "-g", "daemon off;"]
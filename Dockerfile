FROM openjdk:21-jdk
WORKDIR /app

# Copy files
COPY target/trading-0.1-SNAPSHOT.jar app.jar
COPY .env .env

# Expose port
EXPOSE 8080

# Set environment variables from .env
RUN echo "Loading environment variables..." && \
    export $(grep -v '^#' .env | xargs)

# Run the app
CMD ["sh", "-c", "java -jar app.jar"]

# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory to /app
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY ../../.. /app

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Define environment variable
ENV NAME wallet_application

# Run application when the container launches
CMD ["java", "-jar", "patrik_melander.wallet.jar"]
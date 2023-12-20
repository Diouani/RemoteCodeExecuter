# Use an official OpenJDK runtime as a base image
FROM openjdk:11

# Set the working directory inside the container
WORKDIR /usr/src/app

# Copy the local Java file into the container at the specified directory
COPY /src/main/resources/static/Input.java .

# Compile the Java file
RUN javac Input.java

# Redirect the output to a file
CMD ["java", "Input", ">", "output.txt"]

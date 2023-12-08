# Use an official OpenJDK runtime as a base image
FROM openjdk:11

# Set the working directory inside the container
WORKDIR /usr/src/app

# Copy the local Java file into the container at the specified directory
COPY /home/zyliao/IdeaProjects/RemoteExecution/src/main/resources/static/input.java .

# Compile the Java file
RUN javac input.java

# Redirect the output to a file
CMD ["java", "input", ">", "output.txt"]

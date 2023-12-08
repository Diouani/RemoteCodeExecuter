package com.remoteExecution.Zlauncher.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
@Component
public class DockerRunner {
    @Value("${fichier.chemin}")
    public String cheminDuFichier;

    public DockerRunner() {
    }


    public String executeCode(){

        String javaCodeSnippet =
                "public class Main {\n" +
                        "    public static void main(String[] args) {\n" +
                        "        String longText = \"ThisIsALongText12345because i want it to be like this + \";\n" +
                        "        System.out.println(\"Long Text: \" + longText);\n" +
                        "    }\n" +
                        "}";
System.out.println(cheminDuFichier);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("/home/zyliao/IdeaProjects/RemoteExecution/src/main/resources/static/input.java"))) {
            writer.write(javaCodeSnippet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            // Build the Docker image
            executeCommand("docker build -t your-java-app -f /home/zyliao/IdeaProjects/RemoteExecution/src/main/resources/static/Dockerfile .");

            // Run the Docker container
            executeCommand("docker run --name your-container your-java-app");

            // Copy the output.txt file from the container to the local machine
            executeCommand("docker cp your-container:/usr/src/app/output.txt .");

            // Read and print the content of the output.txt file
            String output = readFromFile("output.txt");
            System.out.println("Output from the Docker container:\n" + output);

            // Cleanup: Remove the container and image
            executeCommand("docker rm your-container");
            executeCommand("docker rmi your-java-app");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }


    private static void executeCommand(String command) throws IOException, InterruptedException {
        Process process = new ProcessBuilder()
                .command("bash", "-c", command)
                .start();

        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("Command execution failed: " + command);
        }
    }

    private static String readFromFile(String filename) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(ProcessBuilder.class.getResourceAsStream("/" + filename)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }




    }


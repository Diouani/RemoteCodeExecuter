package com.remoteExecution.Zlauncher.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DockerRunner {
    @Value("${fichier.chemin}")
    public String cheminDuFichier;

    public DockerRunner() {
    }


    public String executeCode(){

        String javaCodeSnippet =
                "public class Input {\n" +
                        "    public static void main(String[] args) {\n" +
                        "        String longText = \"ThisIsALongText12345because i want it to be like this + \";\n" +
                        "        System.out.println(\"Long Text: \" + longText);\n" +
                        "    }\n" +
                        "}";
System.out.println(System.getProperty("user.dir"));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "/src/main/resources/static/Input.java"))) {
            writer.write(javaCodeSnippet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            // Build the Docker image
            executeCommand("docker build --no-cache -t your-java-app -f " +System.getProperty("user.dir")  +"\\src\\Dockerfile .");

            // Generate a unique container name based on the current timestamp
            String containerName = "your-container-" + getCurrentTimestamp();


            // Run the Docker container with the unique name
            executeCommand("docker run --name " + containerName + " your-java-app");


            // Copy the output.txt file from the container to the local machine
            executeCommand("docker cp " + containerName + ":/usr/src/app/output.txt .");

            // Read and print the content of the output.txt file
            String output = readFromFile("output.txt");
            System.out.println("Output from the Docker container:\n" + output);

            // Cleanup: Remove the container and image
            executeCommand("docker rm -f " + containerName);
            executeCommand("docker rmi -f your-java-app");


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


    private String getCurrentTimestamp() {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                return dateFormat.format(new Date());
            }


    }


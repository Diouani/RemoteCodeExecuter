package com.remoteExecution.Zlauncher;

import com.remoteExecution.Zlauncher.service.DockerRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;

@PropertySource("classpath:application.properties")
@SpringBootApplication
public class ZlauncherApplication {



	public static void main(String[] args) {
		SpringApplication.run(ZlauncherApplication.class, args);
		DockerRunner dockerRunner = new DockerRunner();
		dockerRunner.executeCode();
	}


}

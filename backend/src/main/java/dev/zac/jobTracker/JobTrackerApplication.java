package dev.zac.jobTracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import dev.zac.jobTracker.config.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class JobTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobTrackerApplication.class, args);
	}

}

package com.example.applicationsmanagement;

import com.example.applicationsmanagement.file.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class ApplicationsManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationsManagerApplication.class, args);
	}

}

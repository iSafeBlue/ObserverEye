package org.observer.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"org.observer.boot**","org.observer.base**"})
@EnableJpaRepositories(basePackages = "org.observer.base.repository")
@EntityScan(basePackages = "org.observer.base.dto")
@EnableAutoConfiguration
public class ObserverEyeBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(ObserverEyeBootApplication.class, args);
	}

}

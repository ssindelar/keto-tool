package de.sindelar.offdatabaseimport;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = { "de.sindelar" })
@EnableJpaRepositories(basePackages = { "de.sindelar" })
@EntityScan(basePackages = { "de.sindelar" })
public class OffDatabaseImportApplication {

	public static void main(String[] args) throws IOException {
		ConfigurableApplicationContext context = SpringApplication.run(OffDatabaseImportApplication.class, args);
		DatabaseJsonlImport databaseImport = context.getBean(DatabaseJsonlImport.class);
		databaseImport.start();
	}

}

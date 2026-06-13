package com.myflow.my_flow;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootApplication
public class MyFlowApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyFlowApplication.class, args);
	}

	@Bean
	public CommandLineRunner testConnection(DataSource dataSource) {
		return args -> {
			try (Connection connection = dataSource.getConnection()) {
				System.out.println("✅ Database connection successful!");
				System.out.println("DB Url: " + connection.getMetaData().getURL());
			} catch (Exception e) {
				System.err.println("❌ DB connection failed: " + e.getMessage());
				e.printStackTrace();
			}
		};
	}
}

package com.rabobank.jparediscache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringBootJpaApplication
{
	private final CustomerRepository repository;


	@Autowired
	public SpringBootJpaApplication(CustomerRepository repository) {this.repository = repository;}

	public static void main(String[] args)
	{
		SpringApplication.run(SpringBootJpaApplication.class, args);
	}
}
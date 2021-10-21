package com.rabobank.jparediscache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@EnableAutoConfiguration
@SpringBootApplication
@EnableCaching
public class SpringBootJpaApplication
{
	private final CustomerRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(SpringBootJpaApplication.class);

	@Autowired
	public SpringBootJpaApplication(CustomerRepository repository) {this.repository = repository;}

	public static void main(String[] args)
	{
        logger.debug("********************* Started Rabo Assignmet Application");	//, "*********************SpringBootJpaApplication");

		SpringApplication.run(SpringBootJpaApplication.class, args);
	}
}
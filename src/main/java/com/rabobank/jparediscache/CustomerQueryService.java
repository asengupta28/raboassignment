package com.rabobank.jparediscache;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import com.rabobank.jparediscache.CustomerNotFoundException;
import com.rabobank.jparediscache.Customer;
import com.rabobank.jparediscache.CustomerRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

@Service
public class CustomerQueryService
{
	Logger logger = LoggerFactory.getLogger(RaboController.class);

	private final CustomerRepository repository;

	@Autowired
	public CustomerQueryService(CustomerRepository repository) {this.repository = repository;}

	//	This service retrieves Details of all Customers
	//	This meethod is Cache-enabled with Redis server
	//	Service Details:
	//		Type: REST 
	//		Method: GET
	//		Method Parameters / Type: none / none 
	//		Output Type / Details: JSON / Customer ID, First Name, Last Name, Age, Addresss
    @Cacheable(value = "customers")
	public Iterable<Customer> getCustomers()
	{
		System.out.println("(*** SYSOP ***) Get All Customers");
		logn("debug", "Get All Customers");

		return (repository.findAll());
	}

	//	This service retrieves Details of a Customers by its ID
	//	This meethod is Cache-enabled with Redis server
	//	Service Details:
	//		Type: REST 
	//		Method: GET
	//		Method Parameters / Type: ID of a Customer / Long 
	//		Output Type / Details: JSON / Customer ID, First Name, Last Name, Age, Addresss
	@Cacheable(value = "customers", key = "#id")
	public Customer getCustomer(Long id)
	{
		System.out.println("(*** SYSOP ***) Get Customer By ID: " + id);
		logn("debug", "Get Customer By ID: " + id);

		return (repository.findById(id).orElseThrow(CustomerNotFoundException::new));
	}

	//	This service retrieves details of Customers by its First Name
	//	This meethod is Cache-enabled with Redis server
	//	Service Details:
	//		Type: REST 
	//		Method: GET
	//		Method Parameters / Type:
	//			First Name / String
	//			Last Name / String 
	//		Output Type / Details: JSON / Customer ID, First Name, Last Name, Age, Addresss
 	@Cacheable(value = "customers", key = "#firstName")
	public Iterable<Customer> getCustomerByFirstName(String firstName)
	{
		System.out.println("(*** SYSOP ***) Get Customer By First Name: " + firstName);
		logn("debug", "Get Customer By First Name: " + firstName);

		return (repository.getCustomerByFirstName(firstName));
	}
	//	This service retrieves details of Customers by its First Name and Last Name
	//	This meethod is Cache-enabled with Redis server
	//	Service Details:
	//		Type: REST 
	//		Method: GET
	//		Method Parameters / Type:
	//			First Name / String
	//			Last Name / String 
	//		Output Type / Details: JSON / Customer ID, First Name, Last Name, Age, Addresss
 	//@Cacheable(value = "customers", key = "#firstName, #lastName")
	public Iterable<Customer> getCustomerByFirstAndLastName(String firstName, String lastName)
	{
		System.out.println("(*** SYSOP ***) Get Customer By First Name [" + firstName + "] and Last Name [" + lastName + "]");
		logn("debug", "Get Customer By First Name [" + firstName + "] and Last Name [" + lastName + "]");

		//return (customerQueryService.getCustomerByFirstAndLastName(firstName, lastName));
		return (repository.getCustomerByFirstAndLastName(firstName, lastName));
	}

	//	************** UTILITY METHODS **************

	private void logn(String level, Object obj1)
	{
		if(level.equals("debug"))
			logger.debug("****************** LOGGER  Inside CustomerQueryService ****************** " + obj1.toString());
	}
}
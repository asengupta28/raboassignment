package com.rabobank.jparediscache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import com.rabobank.jparediscache.CustomerNotFoundException;
import com.rabobank.jparediscache.Customer;
import com.rabobank.jparediscache.CustomerRepository;

import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CustomerUpdateService
{
	Logger logger = LoggerFactory.getLogger(RaboController.class);

	private final CustomerRepository repository;

	@Autowired
	public CustomerUpdateService(CustomerRepository repository) {this.repository = repository;}


	//	This service Inserts a New Customers
	//	This meethod is Cache-enabled with Redis server
	//	Service Details:
	//		Method Parameters / Type:
	//			FirstName / String
	//			LastName / String
	//			Age / int
	//			Address / String
	//		Output: Long
	@CacheEvict(value = "customers", allEntries=true)
	public Customer addCustomer(Customer customer) throws Exception
	{
		System.out.println("(*** SYSOP ***) Insert Customer : " + customer.getFirstName() + " " + customer.getLastName() + " " + customer.getAge() + " " + customer.getAddress());
		logn("debug", "Insert Customer : " + customer.getFirstName() + " " + customer.getLastName() + " " + customer.getAge() + " " + customer.getAddress());

		return (repository.save(customer));
	}

	//	This service Updates an existing Customers
	//	This meethod is Cache-enabled with Redis server
	//	Service Details:
	//		Request Parameter / Type:
	//			FirstName / String
	//			LastName / String
	//			Age / int
	//			Address / String
	//		Output: Long
	@CacheEvict(value = "customers", allEntries=true)
	public Customer getSaveCustomer(String firstName, String lastName, String age, String address) throws Exception
	{
		System.out.println("(*** SYSOP ***) Insert Customer : ");
		logn("debug", "Insert Customer: " + firstName + " " + lastName + " " + age + " " + address);

		Customer customerToSave = Customer.builder().firstName(firstName).lastName(lastName).age((int)Integer.valueOf(age)).address(address).build();
		Customer newCustomer = repository.save(customerToSave);

		return(newCustomer);
	}


	//	This service Updates Address of an existing Customer
	//	This meethod is Cache-enabled with Redis server
	//	Service Details:
	//		Method Parameters / Type:
	//			ID / Long
	//			Address / String
	//		Output Type / Details: JSON / Customer ID, First Name, Last Name, Age, Addresss
	//@CachePut(value = "customers", key = "#id")
	@CacheEvict(value = "customers", allEntries=true)
	public Customer updateCustomer(Long id, String address) throws Exception
	{
		System.out.println("(*** SYSOP ***) Update Customer Address to " + address + " by ID [" + id + "]");
		logn("debug", "Update Customer Address to " + address + " by ID [" + id + "]");

		Customer customerToUpdate = repository.findById(id).orElseThrow(CustomerNotFoundException::new);
		customerToUpdate.setAddress(address);
		Customer updatedCustomer = repository.save(customerToUpdate);
		logn("debug", "Updated Customer successfully. " + updatedCustomer);

		return (updatedCustomer);
	}
	//	This service Updates Address of an existing Customer
	//	This meethod is Cache-enabled with Redis server
	//	Service Details:
	//		Request Parameter / Type:
	//			ID / Long
	//			Address / String
	//		Output Type / Details: JSON / Customer ID, First Name, Last Name, Age, Addresss
	//@CachePut(value = "customers", key = "#id")
    @CacheEvict(value = "customers", allEntries=true)
	public Customer getUpdateCustomer(Long id, String address) throws Exception
	{
		System.out.println("(*** SYSOP ***) Update Customer Address to " + address + " by ID [" + id + "]");
		logn("debug", "Update Customer Address to " + address + " by ID [" + id + "]");

		Customer customerToUpdate = repository.findById(id).orElseThrow(CustomerNotFoundException::new);
		customerToUpdate.setAddress(address);
		Customer updatedCustomer = repository.save(customerToUpdate);
		logn("debug", "Updated Customer successfully. " + updatedCustomer);

		return (updatedCustomer);
	}

	//	This service Delete an existing Customer
	//	This meethod is Cache-enabled with Redis server
	//	Service Details:
	//		Request Parameter / Type:
	//			ID / Long
	//		Output Type / Details: String / Successs message
    @CacheEvict(value = "customers", allEntries=true)
	public String deleteCustomer(Long id) throws Exception
	{
		Customer.CustomerBuilder existingCustomer = (repository.findById(id).orElseThrow(CustomerNotFoundException::new)).toBuilder();
		repository.deleteById(id);

		return ("Customer [" + id + "] deleted successfully.");
	}
	//	************** UTILITY METHODS **************

	private void logn(String level, Object obj1)
	{
		if(level.equals("debug"))
			logger.debug("****************** LOGGER ****************** " + obj1.toString());
	}
}
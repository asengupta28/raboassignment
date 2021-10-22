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


	 @RestController
@RequestMapping("customers")
public class RaboController
{
	Logger logger = LoggerFactory.getLogger(RaboController.class);

	private final CustomerRepository repository;

	@Autowired
	public RaboController(CustomerRepository repository) {this.repository = repository;}

    @Cacheable(value = "customers")
	@GetMapping
	public Iterable<Customer> getCustomers()
	{
		System.out.println("(*** SYSOP ***) Get All Customers");
		logger.info("(****************** LOGGER ******************) Get All Customers");

		return (repository.findAll());
	}

	@Cacheable(value = "customers", key = "#id")
	@GetMapping("{id}")
	public Customer getCustomer(@PathVariable Long id)
	{
		System.out.println("(*** SYSOP ***) Get Customer By ID: " + id);
		logn("debug", "Get Customer By ID: " + id);

		//try
		{
			return (repository.findById(id).orElseThrow(CustomerNotFoundException::new));
		}
/* 		catch(Exception ex)
		{
			ex.printStackTrace();
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} */
	}

	@CacheEvict(value = "customers", allEntries=true)
	@PostMapping
	public ResponseEntity<?> addCustomer(@RequestBody Customer customer)
	{
		System.out.println("(*** SYSOP ***) Insert Customer : " + customer.getFirstName() + " " + customer.getLastName() + " " + customer.getAge() + " " + customer.getAddress());
		logn("debug", "Insert Customer : " + customer.getFirstName() + " " + customer.getLastName() + " " + customer.getAge() + " " + customer.getAddress());

		try
		{
			return new ResponseEntity<>(repository.save(customer), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CacheEvict(value = "customers", allEntries=true)
    @RequestMapping(value = "/{firstName}/{lastName}/{age}/{address}", method = RequestMethod.GET)
	public ResponseEntity<?> getSaveCustomer(@PathVariable String firstName, @PathVariable String lastName, @PathVariable String age, @PathVariable String address)
	{
		System.out.println("(*** SYSOP ***) Insert Customer : ");
		logn("debug", "Insert Customer: " + firstName + " " + lastName + " " + age + " " + address);

		try
		{
			Customer customerToSave = Customer.builder()
				.firstName(firstName).lastName(lastName).age((int)Integer.valueOf(age)).address(address).build();
			Customer newCustomer = repository.save(customerToSave);
			//logn("Saved Customer successfully. " + newCustomer, "abc");

			return new ResponseEntity<>(newCustomer, HttpStatus.OK);
		}
		catch(Exception ex) {return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);}
	}


	//@CachePut(value = "customers", key = "#id")
	@CacheEvict(value = "customers", allEntries=true)
	@PutMapping("{id}")
	public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody String address)
	{
		System.out.println("(*** SYSOP ***) Update Customer Address to " + address + " by ID [" + id + "]");
		logn("debug", "Update Customer Address to " + address + " by ID [" + id + "]");

		try
		{
			Customer customerToUpdate = repository.findById(id).orElseThrow(CustomerNotFoundException::new);
			customerToUpdate.setAddress(address);
			Customer updatedCustomer = repository.save(customerToUpdate);
			logn("debug", "Updated Customer successfully. " + updatedCustomer);

			return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
		}
		catch(Exception ex) {return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);}
	}
	//@CachePut(value = "customers", key = "#id")
    @CacheEvict(value = "customers", allEntries=true)
    @RequestMapping(value = "/{id}/{address}", method = RequestMethod.GET)
	public ResponseEntity<?> getUpdateCustomer(@PathVariable Long id, @PathVariable String address)
	{
		System.out.println("(*** SYSOP ***) Update Customer Address to " + address + " by ID [" + id + "]");
		logn("debug", "Update Customer Address to " + address + " by ID [" + id + "]");

		try
		{
			Customer customerToUpdate = repository.findById(id).orElseThrow(CustomerNotFoundException::new);
			customerToUpdate.setAddress(address);
			Customer updatedCustomer = repository.save(customerToUpdate);
			logn("debug", "Updated Customer successfully. " + updatedCustomer);

			return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
		}
		catch(Exception ex) {return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);}
	}

/*
    @CacheEvict(value = "customers", allEntries=true)
	@DeleteMapping("/{id}")
	public void deleteCustomer(@PathVariable Long id)
	{
		repository.findById(id).orElseThrow(CustomerNotFoundException::new);
		repository.deleteById(id);
	}

    @CacheEvict(value = "customers", allEntries=true)
	@RequestMapping("delete/{id}")
	public void getDeleteCustomer(@PathVariable Long id)
	{
		Customer.CustomerBuilder existingCustomer = (repository.findById(id).orElseThrow(CustomerNotFoundException::new)).toBuilder();
		//Customer customerToUpdate = existingCustomer.address(address).build();

		//repository.findById(id).orElseThrow(CustomerNotFoundException::new);
		repository.deleteById(id);
		//logn("Customer Deleted successfully.");
	}
 */
	private void logn(String level, Object obj1)
	{
		if(level.equals("debug"))
			logger.debug("****************** LOGGER ****************** " + obj1.toString());
	}
}
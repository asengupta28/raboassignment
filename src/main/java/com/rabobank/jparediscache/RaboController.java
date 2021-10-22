package com.rabobank.jparediscache;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabobank.jparediscache.CustomerNotFoundException;
import com.rabobank.jparediscache.Customer;
import com.rabobank.jparediscache.CustomerRepository;
import com.rabobank.jparediscache.CustomerQueryService;


@RestController
@RequestMapping("customers")
public class RaboController
{
	Logger logger = LoggerFactory.getLogger(RaboController.class);

	private final CustomerRepository repository;

	@Autowired
	CustomerQueryService customerQueryService;

	@Autowired
	public RaboController(CustomerRepository repository) {this.repository = repository;}

	//	This service retrieves Details of all Customers
	//	Service Details:
	//		Type: REST 
	//		Method: GET
	//		Request Parameter / Type: none / none 
	//		Output Type / Details: JSON / Customer ID, First Name, Last Name, Age, Addresss
	@GetMapping
	public Iterable<Customer> getCustomers()
	{
		System.out.println("(*** SYSOP Inside RaboController ***) Get All Customers");
		logn("debug", "Get All Customers");

		return (customerQueryService.getCustomers());
		//return (repository.findAll());
	}

	//	This service retrieves Details of a Customers by its ID
	//	Service Details:
	//		Type: REST 
	//		Method: GET
	//		Request Parameter / Type: ID of a Customer / Long 
	//		Output Type / Details: JSON / Customer ID, First Name, Last Name, Age, Addresss
	@GetMapping("{id}")
	public Customer getCustomer(@PathVariable Long id)
	{
		System.out.println("(*** SYSOP ***) Get Customer By ID: " + id);
		logn("debug", "Get Customer By ID: " + id);

		return (customerQueryService.getCustomer(id));
		//return (repository.findById(id).orElseThrow(CustomerNotFoundException::new));
	}

	//	This service retrieves details of Customers by its First Name
	//	Service Details:
	//		Type: REST 
	//		Method: GET
	//		Request Parameter / Type:
	//			First Name / String
	//			Last Name / String 
	//		Output Type / Details: JSON / Customer ID, First Name, Last Name, Age, Addresss
	@RequestMapping(value = "/name/{firstName}", method = RequestMethod.GET)
	public Iterable<Customer> getCustomerByFirstName(@PathVariable String firstName)
	{
		System.out.println("(*** SYSOP ***) Get Customer By First Name: " + firstName);
		logn("debug", "Get Customer By First Name: " + firstName);

		return (customerQueryService.getCustomerByFirstName(firstName));
		//return (repository.getCustomerByFirstName(firstName));
	}
	//	This service retrieves details of Customers by its First Name and Last Name
	//	Service Details:
	//		Type: REST 
	//		Method: GET
	//		Request Parameter / Type:
	//			First Name / String
	//			Last Name / String
	//		Output Type / Details: JSON / Customer ID, First Name, Last Name, Age, Addresss
	@RequestMapping(value = "/name/{firstName}/{lastName}", method = RequestMethod.GET)
	public Iterable<Customer> getCustomerByFirstAndLastName(@PathVariable String firstName, @PathVariable String lastName)
	{
		System.out.println("(*** SYSOP ***) Get Customer By First Name [" + firstName + "] and Last Name [" + lastName + "]");
		logn("debug", "Get Customer By First Name [" + firstName + "] and Last Name [" + lastName + "]");

		return (customerQueryService.getCustomerByFirstAndLastName(firstName, lastName));
		//return (repository.getCustomerByFirstAndLastName(firstName, lastName));
	}

	//	This service Inserts a New Customers
	//	Service Details:
	//		Type: REST
	//		Method: POST
	//		Request Body / Type:
	//				FirstName / String
	//				LastName / String
	//				Age / int
	//				Address / String
	//		Output: Long
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

	//	This service Updates an existing Customers
	//	Service Details:
	//		Type: REST
	//		Method: GET
	//		Request Parameter / Type:
	//				FirstName / String
	//				LastName / String
	//				Age / int
	//				Address / String
	//		Output: Long
	@CacheEvict(value = "customers", allEntries=true)
    @RequestMapping(value = "/{firstName}/{lastName}/{age}/{address}", method = RequestMethod.GET)
	public ResponseEntity<?> getSaveCustomer(@PathVariable String firstName, @PathVariable String lastName, @PathVariable String age, @PathVariable String address)
	{
		System.out.println("(*** SYSOP ***) Insert Customer : ");
		logn("debug", "Insert Customer: " + firstName + " " + lastName + " " + age + " " + address);

		try
		{
			Customer customerToSave = Customer.builder().firstName(firstName).lastName(lastName).age((int)Integer.valueOf(age)).address(address).build();
			Customer newCustomer = repository.save(customerToSave);

			return new ResponseEntity<>(newCustomer, HttpStatus.OK);
		}
		catch(Exception ex) {return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);}
	}


	//	This service Updates ADdress of an existing Customer
	//	Service Details:
	//		Type: REST
	//		Method: POST
	//		Request Parameter / Type:
	//				ID / Long
	//		Request Body / Type:
	//				Address / String
	//		Output Type / Details: JSON / Customer ID, First Name, Last Name, Age, Addresss
	//@CachePut(value = "customers", key = "#id")
	@CacheEvict(value = "customers", allEntries=true)
	@PutMapping("/mod/{id}")
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
	//	This service Updates ADdress of an existing Customer
	//	Service Details:
	//		Type: REST
	//		Method: GET
	//		Request Parameter / Type:
	//				ID / Long
	//				Address / String
	//		Output Type / Details: JSON / Customer ID, First Name, Last Name, Age, Addresss
	//@CachePut(value = "customers", key = "#id")
    @CacheEvict(value = "customers", allEntries=true)
    @RequestMapping(value = "/mod/{id}/{address}", method = RequestMethod.GET)
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

	//	This service Delete an existing Customer
	//	Service Details:
	//		Type: REST
	//		Method: POST
	//		Request Parameter / Type:
	//				ID / Long
	//		Output Type / Details: String / Successs message
    @CacheEvict(value = "customers", allEntries=true)
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCustomer(@PathVariable Long id)
	{
		Customer.CustomerBuilder existingCustomer = (repository.findById(id).orElseThrow(CustomerNotFoundException::new)).toBuilder();
		repository.deleteById(id);

		return new ResponseEntity<>("Customer [" + id + "] deleted successfully.", HttpStatus.OK);
	}
	//	This service Delete an existing Customer
	//	Service Details:
	//		Type: REST
	//		Method: GET
	//		Request Parameter / Type:
	//				ID / Long
	//		Output Type / Details: String / Successs message
    @CacheEvict(value = "customers", allEntries=true)
	@RequestMapping("delete/{id}")
	public ResponseEntity<?> getDeleteCustomer(@PathVariable Long id)
	{
		Customer.CustomerBuilder existingCustomer = (repository.findById(id).orElseThrow(CustomerNotFoundException::new)).toBuilder();
		repository.deleteById(id);

		return new ResponseEntity<>("Customer [" + id + "] deleted successfully.", HttpStatus.OK);
	}

	//	************** UTILITY METHODS **************

	private void logn(String level, Object obj1)
	{
		if(level.equals("debug"))
			logger.debug("****************** LOGGER Inside RaboController ****************** " + obj1.toString());
	}
}
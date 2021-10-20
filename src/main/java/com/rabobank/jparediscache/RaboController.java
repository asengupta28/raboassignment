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

@RestController
@RequestMapping("customers")
public class RaboController
{
	private final CustomerRepository repository;

	@Autowired
	public RaboController(CustomerRepository repository) {this.repository = repository;}

    @Cacheable(value = "customers")
	@GetMapping
	public Iterable<Customer> getCustomers()
	{
		System.out.println("Get All Customers"); 
		return (repository.findAll());
	}

	@Cacheable(value = "customers", key = "#id")
	@GetMapping("{id}")
	public Customer getCustomer(@PathVariable Long id)
	{
		System.out.println("Get Customer by Id [" + id + "]"); 
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
		System.out.println("Insert Customer : " + customer.getFirstName() + " " + customer.getLastName() + " " + 
							customer.getAge() + " " + customer.getAddress());
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
		System.out.println("Insert Customer : " + firstName + " " + lastName + " " + age + " " + address);

		try
		{
			Customer customerToSave = Customer.builder()
				.firstName(firstName).lastName(lastName).age((int)Integer.valueOf(age)).address(address).build();
			Customer newCustomer = repository.save(customerToSave);
			System.out.println("Saved Customer successfully. " + newCustomer);

			return new ResponseEntity<>(newCustomer, HttpStatus.OK);
		}
		catch(Exception ex) {return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);}
	}


	//@CachePut(value = "customers", key = "#id")
	@CacheEvict(value = "customers", allEntries=true)
	@PutMapping("{id}")
	public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody String address)
	{
		System.out.println("Update Customer: Id [" + id + "] Address [" + address + "]");
		try
		{
			Customer customerToUpdate = repository.findById(id).orElseThrow(CustomerNotFoundException::new);
			customerToUpdate.setAddress(address);
			Customer updatedCustomer = repository.save(customerToUpdate);
			System.out.println("Updated Customer successfully. " + updatedCustomer);

			return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
		}
		catch(Exception ex) {return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);}
	}
	//@CachePut(value = "customers", key = "#id")
    @CacheEvict(value = "customers", allEntries=true)
    @RequestMapping(value = "/{id}/{address}", method = RequestMethod.GET)
	public ResponseEntity<?> getUpdateCustomer(@PathVariable Long id, @PathVariable String address)
	{
		System.out.println("Update Customer: Id [" + id + "] Address [" + address + "]");

		try
		{
			Customer customerToUpdate = repository.findById(id).orElseThrow(CustomerNotFoundException::new);
			customerToUpdate.setAddress(address);
			Customer updatedCustomer = repository.save(customerToUpdate);
			System.out.println("Updated Customer successfully. " + updatedCustomer);

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
		System.out.println("Customer Deleted successfully.");
	}
 */}
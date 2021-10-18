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
	public Iterable<Customer> getCustomers() {return repository.findAll();}	

    @Cacheable(value = "customers", key = "#id")
	@GetMapping("{id}")
	public Customer getCustomer(@PathVariable Long id) {return repository.findById(id).orElseThrow(CustomerNotFoundException::new);}

    @CacheEvict(value = "customers", allEntries=true)
	@PostMapping
	public Customer addCustomer(@RequestBody Customer customer) {return repository.save(customer);}


    @CacheEvict(value = "customers", allEntries=true)
    @RequestMapping(value = "/{firstName}/{lastName}/{age}/{address}", method = RequestMethod.GET)
	public ResponseEntity<?> /* Customer */ getSaveCustomer(@PathVariable String firstName, @PathVariable String lastName, @PathVariable String age, @PathVariable String address)
	{
		System.out.println("Save Customer called: " + firstName + " " + lastName + " " + age + " " + address);

		try
		{
			Customer customerToSave = new Customer();
			customerToSave.setFirstName(firstName);
			customerToSave.setLastName(lastName);
			customerToSave.setAge(Integer.valueOf(age));
			customerToSave.setAddress(address);

			System.out.println("Saved Customer successfully.");
			//return new ResponseEntity<>(repository.save(customerToSave), HttpStatus.OK);
			Customer newCustomer = repository.save(customerToSave);
			return new ResponseEntity<>(newCustomer.getId(), HttpStatus.OK);
		}
		catch(Exception ex) {return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);}
	}


    //@CachePut(value = "customers", key = "#id")
    @CacheEvict(value = "customers", allEntries=true)
	@PutMapping("{id}")
	public ResponseEntity<?> /* Customer */ updateCustomer(@PathVariable Long id, @RequestBody Customer customer)
	{
		try
		{
			Customer customerToUpdate = repository.findById(id).orElseThrow(CustomerNotFoundException::new);
			customerToUpdate.setFirstName(customer.getFirstName());
			customerToUpdate.setLastName(customer.getLastName());
			customerToUpdate.setAge(customer.getAge());
			customerToUpdate.setAddress(customer.getAddress());

			System.out.println("Updated Customer successfully.");
			Customer newCustomer = repository.save(customerToUpdate);
			return new ResponseEntity<>(newCustomer, HttpStatus.OK);
		}
		catch(Exception ex) {return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);}
	}

    //@CachePut(value = "customers", key = "#id")
    @CacheEvict(value = "customers", allEntries=true)
    @RequestMapping(value = "/{id}/{firstName}/{lastName}/{age}/{address}", method = RequestMethod.GET)
	public Customer getUpdateCustomer(@PathVariable Long id, @PathVariable String firstName, @PathVariable String lastName, @PathVariable int age, @PathVariable String address)
	{
		System.out.println(id + " " + firstName + " " + lastName + " " + age + " " + address);

		Customer customerToUpdate = repository.findById(id).orElseThrow(CustomerNotFoundException::new);
		customerToUpdate.setFirstName(firstName);
		customerToUpdate.setLastName(lastName);
		customerToUpdate.setAge(age);
		customerToUpdate.setAddress(address);

		return repository.save(customerToUpdate);
	}


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
		repository.findById(id).orElseThrow(CustomerNotFoundException::new);
		repository.deleteById(id);
	}
}
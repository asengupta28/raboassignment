package com.rabobank.jparediscache;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import com.rabobank.jparediscache.Customer;

public interface CustomerCustomRepository
{
	 List<Customer> getCustomerByFirstName(String firstName);
	 List<Customer> getCustomerByFirstAndLastName(String firstName, String lastName);
}
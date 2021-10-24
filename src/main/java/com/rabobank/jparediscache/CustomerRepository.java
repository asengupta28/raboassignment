package com.rabobank.jparediscache;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import com.rabobank.jparediscache.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long>, CustomerCustomRepository
{
}
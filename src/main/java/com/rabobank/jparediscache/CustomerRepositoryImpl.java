package com.rabobank.jparediscache;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.rabobank.jparediscache.Customer;
import com.rabobank.jparediscache.CustomerNotFoundException;

@Repository
@Transactional(readOnly = true)
public class CustomerRepositoryImpl implements CustomerCustomRepository
{
	@PersistenceContext
    EntityManager entityManager;

	@Override
	public List<Customer> getCustomerByFirstName(String firstName) throws CustomerNotFoundException
	{
		Query query = entityManager.createNativeQuery("SELECT cust.* FROM CUSTOMER as cust WHERE cust.first_name = ?", Customer.class);
		query.setParameter(1, firstName);

		return query.getResultList();
	}

	@Override
	public List<Customer> getCustomerByFirstAndLastName(String firstName, String lastName) throws CustomerNotFoundException
	{
		Query query = entityManager.createNativeQuery("SELECT cust.* FROM CUSTOMER as cust WHERE cust.first_name = ? and cust.last_name = ?", Customer.class);
		query.setParameter(1, firstName);
		query.setParameter(2, lastName);

		return query.getResultList();
	}
}
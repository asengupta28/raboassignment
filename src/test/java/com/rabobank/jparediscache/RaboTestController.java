package com.rabobank.jparediscache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class RaboTestController
{
	@Autowired
	private MockMvc mockMvc;

	String successfulJson = "";
	String incorrectEndBalanceJson = "";

	public static final String SUCCESSFUL = "SUCCESSFUL";
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";

	@Test
	@Order(1)
	public void testGetAllCustomers() throws Exception
	{
		String uri = "/customers/";
		MvcResult mvcResult = null;
		int status = 500;
		try
		{
			mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
		}
		catch(Exception ex) { System.out.println("RaboTestController: Error on testGetAllCustomers -- " + ex.getMessage()); };

		status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		System.out.println("GetAllCustomers called: Status [" + status + "] -- " + content);

		assertEquals(HttpStatus.OK.value(), status);
	}

	@Test
	@Order(2)
	public void testInsertCustomer() throws Exception
	{
		String uri = "/customers";
		String insertJson = "{\"firstName\":\"Saunak\",\"lastName\":\"Sinha\",\"age\": \"40\",\"address\":\"NETHERLANDS\"}";

		MvcResult mvcResult = null;
		int status = 500;
		try
		{
			mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(insertJson)).andReturn();
		}
		catch(Exception ex) { System.out.println("RaboTestController: Error on testInsertCustomer -- " + ex.getMessage()); };

		status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		System.out.println("Insert Customer called: Status [" + status + "] -- " + content);

		ObjectMapper objectMapper = new ObjectMapper();
		com.rabobank.jparediscache.Customer customer = objectMapper.readValue(content, com.rabobank.jparediscache.Customer.class);
		System.out.println("Customer Inserted: " + customer.toString());

		assertEquals(HttpStatus.OK.value(), status);
	}

	@Test
	@Order(3)
	public void testgetCustomerByID() throws Exception
	{
		String uri = "/customers/1115";
		MvcResult mvcResult = null;
		int status = 500;
		try
		{
			System.out.println("URI : " + uri);
			mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
		}
		catch(Exception ex) { System.out.println("RaboTestController: Error on testgetCustomerByID -- " + ex.getMessage()); };

		status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		System.out.println("GetCustomerByID called: Status [" + status + "] -- " + content);

		assertEquals(HttpStatus.OK.value(), status);
	}

	@Test
	@Order(4)
	public void testgetCustomerByFirstName() throws Exception
	{
		String uri = "/customers/name/Abhijit";
		MvcResult mvcResult = null;
		int status = 500;
		try
		{
			System.out.println("URI : " + uri);
			mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
		}
		catch(Exception ex) { System.out.println("RaboTestController: Error on testgetCustomerByID -- " + ex.getMessage()); };

		status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		System.out.println("GetCustomerByID called: Status [" + status + "] -- " + content);

		assertEquals(HttpStatus.OK.value(), status);
	}

	@Test
	@Order(5)
	public void testgetCustomerByFirstNameAndLastName() throws Exception
	{
		String uri = "/customers/name/Abhijit/Sengupta";
		MvcResult mvcResult = null;
		int status = 500;
		try
		{
			System.out.println("URI : " + uri);
			mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
		}
		catch(Exception ex) { System.out.println("RaboTestController: Error on testgetCustomerByID -- " + ex.getMessage()); };

		status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		System.out.println("GetCustomerByID called: Status [" + status + "] -- " + content);

		assertEquals(HttpStatus.OK.value(), status);
	}

 	@Test
	@Order(6)
	public void testUpdateCustomer() throws Exception
	{
		String uri = "/customers/1118";
		String updateJson = "Utrecht NETHERLANDS";

		MvcResult mvcResult = null;
		int status = 500;
		try
		{
			mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(uri).content(updateJson)).andReturn();
		}
		catch(Exception ex) { System.out.println("RaboTestController: Error on testUpdateCustomer -- " + ex.getMessage()); };

		status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		System.out.println("UpdateCustomer  called: Status [" + status + "] -- " + content);

		assertEquals(HttpStatus.OK.value(), status);
	}
}
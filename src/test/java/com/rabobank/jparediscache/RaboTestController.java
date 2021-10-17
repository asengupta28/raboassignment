package com.rabobank.jparediscache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/* import static com.assignment.customer.common.ApplicationConstants.SUCCESSFUL;
import static com.assignment.customer.common.ApplicationConstants.BAD_REQUEST;
import static com.assignment.customer.common.ApplicationConstants.DUPLICATE_REFERENCE;
import static com.assignment.customer.common.ApplicationConstants.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE;
import static com.assignment.customer.common.ApplicationConstants.INCORRECT_END_BALANCE;
import static com.assignment.customer.common.ApplicationConstants.INTERNAL_SERVER_ERROR;
 */

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

//import com.assignment.customer.bean.PostProcessingResult;

@SpringBootTest
@AutoConfigureMockMvc
public class RaboTestController //extends CustomerStatementProcessorTests
{
	@Autowired
	private MockMvc mockMvc;

	String successfulJson = "";

	String incorrectEndBalanceJson = "";

	public static final String SUCCESSFUL = "SUCCESSFUL";
    public static final String DUPLICATE_REFERENCE = "DUPLICATE_REFERENCE";
    public static final String INCORRECT_END_BALANCE = "INCORRECT_END_BALANCE";
    public static final String DUPLICATE_REFERENCE_INCORRECT_END_BALANCE = "DUPLICATE_REFERENCE_INCORRECT_END_BALANCE";
    public static final String BAD_REQUEST = "BAD_REQUEST";
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    public static final String RETRIEVE_INTERNAL_SERVER_TEST_PROPERTY_DATA = "INTERNAL_SERVER_ERROR_TEST_DATA";

	@Test
	public void testGetSaveCustomer() throws Exception
	{
		String uri = "/customers/New Customer FName/New Customer LName/55/New Customer Address";
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(HttpStatus.OK.value(), status);
		String content = mvcResult.getResponse().getContentAsString();
		System.out.println("Save Customer called: " + content);
		//PostProcessingResult response = super.mapFromJson(content, PostProcessingResult.class);
		assertNotNull(content);	//response.getResult());
	}

 	@Test
	public void testUpdateCustomer() throws Exception
	{
		String uri = "/customers/1021/New Customer 1/New Customer 1/75/New Customer Address1";
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(HttpStatus.OK.value(), status);
		String content = mvcResult.getResponse().getContentAsString();
		//PostProcessingResult response = super.mapFromJson(content, PostProcessingResult.class);
		assertNotNull(content);	//response.getResult());
	}
}
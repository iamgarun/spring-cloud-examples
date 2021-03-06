package com.example.reservations;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReservationServiceApplicationTests {

	@Autowired
	WebApplicationContext webApplicationContext;

	MockMvc mockMVC;


	@Before
	public void before() {
		mockMVC = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void contextLoads() throws Exception{
		mockMVC.perform(MockMvcRequestBuilders.get("/reservations"))
		     .andExpect(MockMvcResultMatchers.status().isOk())
		     .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json;charset=UTF-8"));
	}
}

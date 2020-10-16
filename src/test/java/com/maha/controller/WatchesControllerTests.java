/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.maha.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class WatchesControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void noParamGreetingShouldReturnDefaultMessage() throws Exception {

		// Check if all spring dependencies are wired correctly and webservice is
		// executable
		this.mockMvc.perform(get("/allWatches")).andDo(print()).andExpect(status().isCreated());
	}

	@Test
	public void checkDiscountedPrice() throws Exception {

		this.mockMvc.perform(get("/checkout").param("watch_ids", new String[] { "001", "001", "001" }))
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.price").value("200"));
	}
	
	@Test
	public void checkDiscountedPrice_MoreThanOneBundleBought() throws Exception {

		this.mockMvc
				.perform(get("/checkout").param("watch_ids", new String[] { "002", "002", "002", "002", "002", "002" }))
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.price").value("360"));
	}

	@Test
	public void checkDiscountedPrice_AndQuantityIsNotExactlyMultipleOfDiscountQuantity() throws Exception {

		this.mockMvc.perform(get("/checkout").param("watch_ids", new String[] { "001", "001", "001", "001" }))
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.price").value("300"));
	}

	@Test
	public void checkNonDiscountedPrice_MixedItems() throws Exception {

		this.mockMvc.perform(get("/checkout").param("watch_ids", new String[] { "001", "002", "004", "004" }))
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.price").value("240"));
	}

	@Test
	public void checkIfNothingBought() throws Exception {

		this.mockMvc.perform(get("/checkout").param("watch_ids", new String[] { "" }))
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.price").value("0"));
	}
}

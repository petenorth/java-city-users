/*
 * Copyright 2012-2015 the original author or authors.
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

package com.example.consumingrest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class UsersClientTest {

	private static final Double LONDON_LATITUDE = 51.5074;

	private static final Double LONDON_LONGITUDE = 0.1278;
	
	private static final String LONDON_CITY = "London";

	@MockBean
	private RestTemplate restTemplate;
	
	@Autowired
	private UsersClient usersClient;
	

	@Test
	public void contextLoads() {
		assertThat(restTemplate).isNotNull();
	}
	
	@Test
	public void testGetUsersWithinDistanceOf() {
		User user = new User(266L, "Ancell", "Garnsworthy", "agarnsworthy7d@seattletimes.com", "67.4.69.137", 51.6553959, 0.0572553);
		List<User> users = new ArrayList<>();
		users.add(user);
		ResponseEntity<List<User>> responseEntity = new ResponseEntity<List<User>>(users, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(UsersClient.API_URL_BASE + "/users",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {})).thenReturn(responseEntity);
		List<User> results = usersClient.getUsersWithInDistance(50.0, LONDON_LATITUDE, LONDON_LONGITUDE);
		assertEquals(1, results.size());
		assertEquals(user.toString(), results.get(0).toString());
		Mockito.verify(restTemplate).exchange(UsersClient.API_URL_BASE + "/users",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {});
	}

	@Test
	public void testGetUsersWithinDistanceOfOutsideOf() {
		User user = new User(266L, "Ancell", "Garnsworthy", "agarnsworthy7d@seattletimes.com", "67.4.69.137", 61.6553959, 0.0572553);
		List<User> users = new ArrayList<>();
		users.add(user);
		ResponseEntity<List<User>> responseEntity = new ResponseEntity<List<User>>(users, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(UsersClient.API_URL_BASE + "/users",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {})).thenReturn(responseEntity);
		List<User> results = usersClient.getUsersWithInDistance(50.0, LONDON_LATITUDE, LONDON_LONGITUDE);
		assertEquals(0, results.size());
		Mockito.verify(restTemplate).exchange(UsersClient.API_URL_BASE + "/users",
				HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {});
	}

	@Test
	public void testGetUsersWithinDistanceOfServerError() {
		ResponseEntity<List<User>> responseEntity = new ResponseEntity<List<User>>(HttpStatus.INTERNAL_SERVER_ERROR);
		Mockito.when(restTemplate.exchange(UsersClient.API_URL_BASE + "/users",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {})).thenReturn(responseEntity);
		ServiceException exception = assertThrows(ServiceException.class, () -> {
			usersClient.getUsersWithInDistance(50.0, LONDON_LATITUDE, LONDON_LONGITUDE);
		});
		assertEquals("<500 INTERNAL_SERVER_ERROR Internal Server Error,[]>", exception.getMessage());
		Mockito.verify(restTemplate).exchange(UsersClient.API_URL_BASE + "/users",
				HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {});
	}
	
	@Test
	public void testGetUsersInCity() {
		User user = new User(266L, "Ancell", "Garnsworthy", "agarnsworthy7d@seattletimes.com", "67.4.69.137", 51.6553959, 0.0572553);
		List<User> users = new ArrayList<>();
		users.add(user);
		ResponseEntity<List<User>> responseEntity = new ResponseEntity<List<User>>(users, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(UsersClient.API_URL_BASE + "/city/" + LONDON_CITY + "/users" ,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {})).thenReturn(responseEntity);
		List<User> results = usersClient.getUsersInCity(LONDON_CITY);
		assertEquals(1, results.size());
		assertEquals(user.toString(), results.get(0).toString());
		Mockito.verify(restTemplate).exchange(UsersClient.API_URL_BASE + "/city/" + LONDON_CITY + "/users",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {});
	}

	@Test
	public void testGetUsersInCityEmpty() {
		ResponseEntity<List<User>> responseEntity = new ResponseEntity<List<User>>(new ArrayList<User>(), HttpStatus.OK);
		Mockito.when(restTemplate.exchange(UsersClient.API_URL_BASE + "/city/" + LONDON_CITY + "/users" ,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {})).thenReturn(responseEntity);
		List<User> results = usersClient.getUsersInCity(LONDON_CITY);
		assertEquals(0, results.size());
		Mockito.verify(restTemplate).exchange(UsersClient.API_URL_BASE + "/city/" + LONDON_CITY + "/users",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {});
	}
	
	@Test
	public void testGetUsersInCityServerError() {
		ResponseEntity<List<User>> responseEntity = new ResponseEntity<List<User>>(HttpStatus.INTERNAL_SERVER_ERROR);
		Mockito.when(restTemplate.exchange(UsersClient.API_URL_BASE + "/city/" + LONDON_CITY + "/users" ,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {})).thenReturn(responseEntity);
		ServiceException exception = assertThrows(ServiceException.class, () -> {
			usersClient.getUsersInCity(LONDON_CITY);
		});
		assertEquals("<500 INTERNAL_SERVER_ERROR Internal Server Error,[]>", exception.getMessage());
		Mockito.verify(restTemplate).exchange(UsersClient.API_URL_BASE + "/city/" + LONDON_CITY + "/users",
				HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {});
	}


}

package com.example.consumingrest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

class ListMergerTest {

	@Test
	void testMerge() {
		
		User user1 = new User(266L, "John", "Arnside", "johnarnside@seattletimes.com", "67.4.69.137", 51.6553959, 0.0572553);
		User user2 = new User(267L, "Elizabeth", "Silverdale", "elizabethsilverdale@seattletimes.com", "68.4.69.137", 52.6553959, 0.0672553);
		User user3 = new User(268L, "Thomas", "Carnforth", "thomascarnforth@seattletimes.com", "69.4.69.137", 53.6553959, 0.0772553);
		
		List<User> users1 = new ArrayList<>();
		List<User> users2 = new ArrayList<>();
		users1.add(user1);
		users1.add(user2);
		users2.add(user3);
		
		ListMerger merger = new ListMerger();
		List<User> result = merger.merge(users1, users2);
		
		assertEquals(3, result.size());
		assertTrue(result.stream().map(x -> x.getFirst_name()).collect(Collectors.toList()).contains("John"));
		assertTrue(result.stream().map(x -> x.getFirst_name()).collect(Collectors.toList()).contains("Elizabeth"));
		assertTrue(result.stream().map(x -> x.getFirst_name()).collect(Collectors.toList()).contains("Thomas"));
		
	}
	
	@Test
	void testMergeEmptyEmpty() {
		
		List<User> users1 = new ArrayList<>();
		List<User> users2 = new ArrayList<>();
		
		ListMerger merger = new ListMerger();
		List<User> result = merger.merge(users1, users2);
		
		assertEquals(0, result.size());
		
	}
	
	@Test
	void testMergeTwoEmpty() {
		
		User user1 = new User(266L, "John", "Arnside", "johnarnside@seattletimes.com", "67.4.69.137", 51.6553959, 0.0572553);
		User user2 = new User(267L, "Elizabeth", "Silverdale", "elizabethsilverdale@seattletimes.com", "68.4.69.137", 52.6553959, 0.0672553);
		
		List<User> users1 = new ArrayList<>();
		List<User> users2 = new ArrayList<>();
		users1.add(user1);
		users1.add(user2);
		
		ListMerger merger = new ListMerger();
		List<User> result = merger.merge(users1, users2);
		
		assertEquals(2, result.size());
		assertTrue(result.stream().map(x -> x.getFirst_name()).collect(Collectors.toList()).contains("John"));
		assertTrue(result.stream().map(x -> x.getFirst_name()).collect(Collectors.toList()).contains("Elizabeth"));
		assertFalse(result.stream().map(x -> x.getFirst_name()).collect(Collectors.toList()).contains("Thomas"));
		
	}

	@Test
	void testMergeEmptyOne() {
		
		User user3 = new User(268L, "Thomas", "Carnforth", "thomascarnforth@seattletimes.com", "69.4.69.137", 53.6553959, 0.0772553);
		
		List<User> users1 = new ArrayList<>();
		List<User> users2 = new ArrayList<>();
		users2.add(user3);
		
		ListMerger merger = new ListMerger();
		List<User> result = merger.merge(users1, users2);
		
		assertEquals(1, result.size());
		assertFalse(result.stream().map(x -> x.getFirst_name()).collect(Collectors.toList()).contains("John"));
		assertFalse(result.stream().map(x -> x.getFirst_name()).collect(Collectors.toList()).contains("Elizabeth"));
		assertTrue(result.stream().map(x -> x.getFirst_name()).collect(Collectors.toList()).contains("Thomas"));
		
	}
}

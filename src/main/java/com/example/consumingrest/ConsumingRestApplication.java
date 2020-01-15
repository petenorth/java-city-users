package com.example.consumingrest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ConsumingRestApplication {

	private static final Logger log = LoggerFactory.getLogger(ConsumingRestApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ConsumingRestApplication.class, args);
	}
	
	@Bean
	public UsersClient usersClient() {
		return new UsersClient();
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	
	@Bean
	public ListMerger listMerger() {
		return new ListMerger();
	}

	@Autowired
	private UsersClient usersClient;

	@Bean
	public CommandLineRunner run() throws Exception {
		return args -> {
			if (args.length != 4) {
				log.info("Usage ConsumingRestApplication <city> <distance in miles> <latitude> <longitude>");
			} else {
		      List<User> usersDistance = usersClient.getUsersWithInDistance(Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
		      List<User> usersCity = usersClient.getUsersInCity(args[0]);
		      listMerger().merge(usersDistance, usersCity).stream().forEach(user -> log.info(user.toString()));
			}
		};
	}
}

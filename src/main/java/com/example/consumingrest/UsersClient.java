package com.example.consumingrest;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class UsersClient {
	
	private static final double METRES_TO_MILES = 0.000621371;
	public static final String API_URL_BASE = "https://bpdts-test-app.herokuapp.com/";
	
	private static final Logger log = LoggerFactory.getLogger(UsersClient.class);
	
	@Autowired
	RestTemplate restTemplate;
	
	public List<User> getUsersWithInDistance(Double distance, Double latitude, Double longitude){
		ResponseEntity<List<User>> userResponse = restTemplate.exchange(API_URL_BASE + "/users",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {
        });
		if ( HttpStatus.OK == userResponse.getStatusCode() ) {
            List<User> users = userResponse.getBody();
            return users.stream().filter(user -> getDistance(user, latitude, longitude) < distance).collect(Collectors.toList());
		} else {
			throw new ServiceException(userResponse.toString());
		}
	}
	
	public List<User> getUsersInCity(String city){
		ResponseEntity<List<User>> userResponse = restTemplate.exchange(API_URL_BASE + "/city/" + city + "/users",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {
        });
		if ( HttpStatus.OK == userResponse.getStatusCode() ) {
          return userResponse.getBody();
		} else {
			throw new ServiceException(userResponse.toString());
		}        
	}
	
	public List<User> getUsersInCityOrWithinDistanceOfCity(String city, Double distance, Double latitude, Double longitude){
	    List<User> users1 = getUsersWithInDistance(distance, latitude, longitude);
	    List<User> users2 = getUsersInCity(city);
	    
	    List<Long> userIds1 = users1.stream()
	    	     .map(x -> x.getId()).collect(Collectors.toList()); 

	    users2.stream()
	    	  .filter( x->!userIds1.contains(x.getId()) )
	    	  .forEach( x-> users1.add(x) );
	    return users1;
    }
	    	     
	    
	
	private double getDistance(User user, Double latitude, Double longitude) {
		return org.apache.lucene.util.SloppyMath.haversinMeters(user.getLatitude(), user.getLongitude(), latitude, longitude) * METRES_TO_MILES;
	}

}

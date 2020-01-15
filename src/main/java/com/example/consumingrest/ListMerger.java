package com.example.consumingrest;

import java.util.List;
import java.util.stream.Collectors;

public class ListMerger {

	public List<User> merge(List<User> list1, List<User> list2){
	    
	    List<Long> userIds1 = list1.stream()
	    	     .map(x -> x.getId()).collect(Collectors.toList()); 

	    list2.stream()
	    	  .filter( x->!userIds1.contains(x.getId()) )
	    	  .forEach( x-> list1.add(x) );
	    return list1;
    }
}

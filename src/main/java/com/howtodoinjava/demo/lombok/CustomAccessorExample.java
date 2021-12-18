package com.howtodoinjava.demo.lombok;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class CustomAccessorExample {
	public static void main(String[] args) {
		Tag tag = new Tag();
		
		tag.setId(1l);
		tag.setName("Test");
		
		System.out.println(tag);
	}
}

@Data
class Tag 
{
	private long id;
	private String name;
	
	@Setter(AccessLevel.NONE)
	private boolean status;
}
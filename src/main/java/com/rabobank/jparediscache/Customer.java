package com.rabobank.jparediscache;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


@Entity
@Getter @Setter @NoArgsConstructor
public class Customer implements Serializable
{
    private static final long serialVersionUID = 7156526077883281623L;

	@Id
	//@GeneratedValue(strategy=GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String firstName;

	private String lastName;

	private int age;

	private String address;

/* 	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	public String getFirstName() {return firstName;}
	public void setFirstName(String firstName) {this.firstName = firstName;}

	public String getLastName() {return lastName;}
	public void setLastName(String lastName) {this.lastName = lastName;}

	public int getAge() {return age;}
	public void setAge(int age) {this.age = age;}	

	public String getAddress() {return address;}
	public void setAddress(String address) {this.address = address;}
*/
}
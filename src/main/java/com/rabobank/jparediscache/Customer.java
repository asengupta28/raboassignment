package com.rabobank.jparediscache;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.io.Serializable;

import lombok.Builder;
import lombok.NonNull;

import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain=true)
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
//@Table(name = "customer")

public class Customer implements Serializable
{
	private static final long serialVersionUID = 7156526077883281623L;

	//private static long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message = "First name is required")
	private String firstName;

	@NotEmpty(message = "Last name is required")
	private String lastName;

	@Min(18l)
	@Max(100l)
	private int age;

	@NotEmpty(message = "Address is required")
	private String address;
}